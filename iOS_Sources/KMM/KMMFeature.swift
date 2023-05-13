//
//  KMMFeatures.swift
//  PredictorSDK
//
//  Created by Sergei Mikhan on 15.06.21.
//  Copyright © 2021 Origins-digital. All rights reserved.
//

import UIKit
import predictorShared

protocol Lifecycleble {
  func onStart()
  func onStop()
}

protocol ScenableState {

  var scene: SceneKMM { get }
}

protocol StatefullFeature: NSObject {

  associatedtype State: NSObject
}

protocol OutputEventableFeature: NSObject {

  associatedtype OutputEvent: NSObject
}

protocol InputableFeature: NSObject {

  associatedtype Input: NSObject

  static var appAppearInput: Input? { get }

  static var appearInput: Input? { get }
  static var disappearInput: Input? { get }

  static var refreshInput: Input? { get }
  static var loadMoreInput: Input? { get }

  // FIXME: not really sure that we need this on ios
  // temporary keeping it here
  static var restartInput: Input? { get }
}

extension InputableFeature {

  static var appAppearInput: Input? { nil }
  static var appearInput: Input? { nil }
  static var disappearInput: Input? { nil }
  static var refreshInput: Input? { appearInput }
  static var loadMoreInput: Input? { nil }
  static var restartInput: Input? { nil }
}

typealias KMMFeature = StatefullFeature & OutputEventableFeature & InputableFeature

struct StoreAndData<Store: Hashable, Data>: Hashable {

  public func hash(into hasher: inout Hasher) {
    hasher.combine(store)
  }

  public static func == (lhs: StoreAndData<Store, Data>, rhs: StoreAndData<Store, Data>) -> Bool {
    return lhs.store == rhs.store
  }

  public let store: Store
  public let data: Data

  public init(store: Store, data: Data) {
    self.store = store
    self.data = data
  }
}

struct SwiftWrapperStateFlowWrapper<T> where T: KMMObjcType {
  public init(flowWrapper: StateFlowWrapper<T>) {
     self.flowWrapper = flowWrapper
   }

  let flowWrapper: StateFlowWrapper<T>

  func subscribe(onNext closure: @escaping ((T) -> ())) -> predictorShared.Job {
    return flowWrapper.subscribe(onEach: closure)
  }

  public func asDriver() -> Driver<T.SwiftType> {
    return Observable<T>.create { observer -> Disposable in
      let job = self.subscribe(onNext: { event in
        observer.onNext(event)
      })
      return Disposables.create {
        job.cancel(cause: .init(message: "Unsubscribed"))
      }
    }.map { $0.asSwiftEntity() }
      .distinctUntilChanged()
      .observe(on: ConcurrentMainScheduler.instance)
      .asDriver(onErrorRecover: { _ in .empty() })
  }

    func asObservable() -> Observable<T.SwiftType> {
      return Observable<T>.create { observer -> Disposable in
        let job = self.subscribe(onNext: { event in
          observer.onNext(event)
        })
        return Disposables.create {
          job.cancel(cause: .init(message: "Unsubscribed"))
        }
      }.map { $0.asSwiftEntity() }
      .distinctUntilChanged()
    }
}

struct SwiftWrapperFlowWrapper<T> where T: KMMObjcType {
  let flowWrapper: FlowWrapper<T>

  func subscribe(onNext closure: @escaping ((T) -> ())) -> predictorShared.Job {
    return flowWrapper.subscribe(onEach: closure)
  }

  func asDriver() -> Driver<T.SwiftType> {
    return Observable<T>.create { observer -> Disposable in
      let job = self.subscribe(onNext: { event in
        observer.onNext(event)
      })
      return Disposables.create {
        job.cancel(cause: .init(message: "Unsubscribed"))
      }
    }.map { $0.asSwiftEntity() }
      .observe(on: ConcurrentMainScheduler.instance)
      .asDriver(onErrorRecover: { _ in .empty() })
  }
  func asObservable() -> Observable<T.SwiftType> {
     return Observable<T>.create { observer -> Disposable in
       let job = self.subscribe(onNext: { event in
         observer.onNext(event)
       })
       return Disposables.create {
         job.cancel(cause: .init(message: "Unsubscribed"))
       }
     }.map { $0.asSwiftEntity() }
   }
}
