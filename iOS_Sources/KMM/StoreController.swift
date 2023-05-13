//
//  StoreController.swift
//  PredictorSDK
//
//  Created by Sergei Mikhan on 3.08.21.
//  Copyright © 2021 Origins-digital. All rights reserved.
//

import Foundation
import predictorShared

typealias FeatureStore<UIState: AnyObject, Output: AnyObject> = predictorShared.Store<UIState, Output>

struct StoreHostState<UIState: KMMSwiftType, Output: KMMSwiftType>
    where UIState.ObjcType.SwiftType == UIState, Output.ObjcType.SwiftType == Output {
  public var storeBindDisposeBag = DisposeBag()
  public let stateRelay = PublishRelay<UIState>()
  public let outputRelay = PublishRelay<Output>()
}

protocol StoreHost: AnyObject {
  associatedtype UIState: KMMSwiftType where UIState.ObjcType.SwiftType == UIState
  associatedtype Output: KMMSwiftType where Output.ObjcType.SwiftType == Output

  typealias OutputEvent = Output
  typealias State = UIState
  typealias Store = FeatureStore<UIState.ObjcType, Output.ObjcType>

  var store: FeatureStore<UIState.ObjcType, Output.ObjcType>? { get }

  var hostState: StoreHostState<UIState, Output> { get set }

  func handle(state: State)
  func handle(action: OutputEvent)
}

extension StoreHost {

  public var currentState: State? {
    return store?.state.value.asSwiftEntity()
  }

  public var state: Driver<State> {
    return hostState.stateRelay.asDriver(onErrorRecover: { _ in .empty() })
  }

  public var output: Driver<OutputEvent> {
    return hostState.outputRelay.asDriver(onErrorRecover: { _ in .empty() })
  }

  public func subscribe(to store: Store?) {
    hostState.storeBindDisposeBag = DisposeBag()
    guard let store = store else {
      return
    }
    let state = SwiftWrapperStateFlowWrapper(flowWrapper: store.state)
    let outputEvents = SwiftWrapperFlowWrapper(flowWrapper: store.outputEvents)

    state.asObservable()
      .observe(on: ConcurrentMainScheduler.instance)
      .do(onNext: { [weak self] in self?.handle(state: $0)})
        .bind(to: hostState.stateRelay)
      .disposed(by: hostState.storeBindDisposeBag)

    outputEvents.asObservable()
      .observe(on: ConcurrentMainScheduler.instance)
      .do(onNext: { [weak self] in self?.handle(action: $0) })
      .bind(to: hostState.outputRelay)
      .disposed(by: hostState.storeBindDisposeBag)
  }
}

class StoreController<UIState: KMMSwiftType, Output: KMMSwiftType>: CoreUI.ViewController, StoreHost
where UIState.ObjcType.SwiftType == UIState, Output.ObjcType.SwiftType == Output {

  public var hostState = StoreHostState<UIState, Output>()

  var store: FeatureStore<UIState.ObjcType, Output.ObjcType>? {
    didSet {
      subscribe(to: store)
    }
  }

  func handle(state: UIState) {

  }

  func handle(action: Output) {

  }

  deinit {
    store?.cancel()
  }
}

enum Internal: Error {
  case error
}

extension ViewStateContainer {

  public func handle(scene: SceneKMM) {
    self.emptyViewSubject.onNext(scene.kmmObjcEntity.isError)
    self.errorSubject.onNext(scene.kmmObjcEntity.isError ? Internal.error : nil)
    self.loaderViewSubject.onNext(scene.kmmObjcEntity.isEmptyLoading)
    self.refreshViewSubject.onNext(scene.kmmObjcEntity.isRefreshing)
  }
}

extension ViewStateContainer where Self: DisposableContainer {

  func bind(scene: Observable<SceneKMM>) {
    scene.map { $0.kmmObjcEntity.isError }.bind(to: emptyViewSubject).disposed(by: disposeBag)
    scene.map { $0.kmmObjcEntity.isError ? Internal.error : nil }.bind(to: errorSubject).disposed(by: disposeBag)
    scene.map { $0.kmmObjcEntity.isEmptyLoading }.bind(to: loaderViewSubject).disposed(by: disposeBag)
    scene.map { $0.kmmObjcEntity.isRefreshing }.bind(to: refreshViewSubject).disposed(by: disposeBag)
  }
}
