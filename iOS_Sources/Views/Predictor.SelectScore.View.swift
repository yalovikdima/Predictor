//
//  Predictor.NumbersView.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 2.02.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

extension Predictor.SelectScore {
  final class View: CoreUI.View, ViewReusable {
    public enum Style: Hashable {
      case normal, share, disabled
    }
    
    public struct ViewModel: Hashable {
      public enum Side {
        case left, right
      }
      
      public let range: RangeUiKMM?
      public let side: Side
      public let selectorBackgoundColor: UIColor
      public let preselected: Int?
      public let style: Style

      public init(range: RangeUiKMM?,
                  selectorBackgoundColor: UIColor,
                  side: Side = .left,
                  preselected: LabelKMM? = nil,
                  style: Style = .normal) {
        self.range = range
        self.side = side
        self.selectorBackgoundColor = selectorBackgoundColor
        self.preselected = preselected.flatMap { Int($0.text) }
        self.style = style
      }
    }

    public typealias Data = ViewModel

    private let selectorView = CoreUI.View()
    private let collectionView = CollectionView<CollectionViewSource>()
    
    fileprivate let actions = PublishSubject<Predictor.SelectScore.Cell.Actions>()
    fileprivate let selected = PublishSubject<Int>()
    fileprivate let scrollCompleate = PublishSubject<Void>()
    fileprivate let resetScore = PublishSubject<Void>()
    
    private var side: ViewStyle.Predictor.View.Side?
    private var cellsViewModels = [Predictor.SelectScore.Cell.ViewModel]()
    
    private var prescroll = false
    private var layoutDisposeBag = DisposeBag()
    private var setupDisposeBag = DisposeBag()
    private var data: Data?
    
    private typealias Cell = CollectionCell<Predictor.SelectScore.Cell>
    
    public override func setup() {
      super.setup()
      
      collectionView.contentInsetAdjustmentBehavior = .never
      collectionView.insetsLayoutMarginsFromSafeArea = false
      
      addSubviews(selectorView, collectionView)
      
      setupCollection()
      
      actions
        .asDriver(onErrorJustReturn: .none)
        .drive(with: self) { owner, value in
          if case let .tap(index) = value {
            owner.collectionView.scroll(to: .init(item: index, section: 0), at: .center, animated: true)
          }
        }.disposed(by: disposeBag)
    }
    
    public override func layoutSubviews() {
      super.layoutSubviews()
      
      if data?.style == .share {
        selectorView.pin.start().bottom().height(40).width(50.ui)
      } else {
        selectorView.pin.start().vCenter().height(40).width(50.ui)
      }
      collectionView.pin.start().vertically().width(50.ui)
      setupSelectorViewStyle()
    }

    public func setup(with model: Data) {
      self.data = model
      
      setupStyle(with: model)
      setupCells(with: model)
      setNeedsLayout()
      self.prescroll = true
      
      let disposeBag = DisposeBag()
      resetScore
        .asDriver(onErrorJustReturn: ())
        .drive(with: self) { owner, _ in
          let totalCount = Int((model.range?.maxValue ?? 9) - (model.range?.minValue ?? 0) + 1)
          let indexPath: IndexPath = .init(item: totalCount - 1, section: 0)
          owner.collectionView.scroll(to: indexPath, at: .center, animated: true)
        }.disposed(by: disposeBag)
      setupDisposeBag = disposeBag
    }
  }
}

private extension Predictor.SelectScore.View {
  func setupStyle(with model: ViewModel) {
    side = model.side == .left ? .left : .right
    setypCollectionLayout(with: model)
    collectionView.isUserInteractionEnabled = !(model.style == .disabled)
  }
  
  func setupSelectorViewStyle() {
    if let side = data?.side,
        let color = data?.selectorBackgoundColor {
      switch side {
      case .left:
        selectorView.viewStyle = ViewStyle.Predictor.View.scoreSelector(side: .left, color: color)
      case .right:
        selectorView.viewStyle = ViewStyle.Predictor.View.scoreSelector(side: .right, color: color)
      }
    }
  }
  
  func setypCollectionLayout(with model: ViewModel) {
    let layout = FadingAutoAlignedLayout(fadeFactor: model.style == .share ? 1.0 : 0.65,
                                         position: model.style == .share ? .bottom : .center,
                                         settings: .init(alignment: model.style == .share ? .end : .center))
    layout.scrollDirection = .vertical
    layout.minimumLineSpacing = 5.ui
    layout.minimumInteritemSpacing = 0.ui
    collectionView.collectionViewLayout = layout
    
    let disposeBag = DisposeBag()
    layout.readyObservable
      .observe(on: MainScheduler.asyncInstance)
      .asDriver(onErrorJustReturn: ())
      .filter { [weak self] _ in return self?.prescroll == true }
      .drive(with: self) { owner, _ in
        owner.prescroll = false
        let max = Int(model.range?.maxValue ?? 9)
        let min = Int(model.range?.minValue ?? 0)
        var position: TargetScrollPosition = .center
        var indexPath: IndexPath = .init(item: max, section: 0)
        if let preselected = model.preselected,
           (min...max) ~= preselected {
          position = model.style == .share ? .end : .center
          indexPath = .init(item: max - preselected, section: 0)
        }
        owner.collectionView.scroll(to: indexPath, at: position, animated: false)
        owner.scrollCompleate.onNext(())
      }.disposed(by: disposeBag)
    layoutDisposeBag = disposeBag
  }
  
  func setupCollection() {
    collectionView.showsHorizontalScrollIndicator = false
    collectionView.showsVerticalScrollIndicator = false
    
    collectionView.rx.didScroll
      .observe(on: MainScheduler.asyncInstance)
      .flatMap { [weak self] _ -> Observable<Int> in
        guard let self = self  else { return .empty() }
        let visibleRect = CGRect(origin: self.collectionView.contentOffset, size: self.collectionView.bounds.size)
        let visibleIndexPath = self.collectionView.indexPathForItem(at: visibleRect.midpoint)
        if let item = visibleIndexPath?.item {
          if let model = self.cellsViewModels[safe: item] {
            return .just(Int(model.numberFocused.string) ?? 0)
          }
        }
        
        return .empty()
      }.bind(to: selected).disposed(by: disposeBag)
  }
  
  func setupCells(with model: ViewModel) {
    guard let min = model.range?.minValue, let max = model.range?.maxValue else { return }
    
    var cellStyle = Predictor.SelectScore.Cell.Style.normal
    switch model.style {
    case .disabled:
      cellStyle = .disabled
    case .share:
      cellStyle = .share
    default: break
    }
    
    cellsViewModels = (min...max).reversed().compactMap { Cell.Data(number: String($0),
                                                                    style: cellStyle) }
    let cells = cellsViewModels.enumerated().compactMap { value -> Cellable in
      Cell(data: value.element,
           id: value.element.number.string,
           eventsEmmiter: actions.asObserver(),
           clickEvent: .tap(index: value.offset))
    }
    
    collectionView.source.sections = [Section(cells: cells)]
    collectionView.reloadData()
  }
}

extension Reactive where Base: Predictor.SelectScore.View {
  var selected: ControlEvent<Int> { .init(events: base.selected) }
  var scrollCompleate: ControlEvent<Void> { .init(events: base.scrollCompleate) }
  var resetScore: Binder<Void> { .init(base) { view, _ in view.resetScore.onNext(()) } }
}
