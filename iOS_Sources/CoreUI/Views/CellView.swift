//
//  Cell.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 3/17/20.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

struct ActionViewModel<ViewModel: Equatable, EventType>: Equatable {
  public static func == (lhs: ActionViewModel<ViewModel, EventType>,
                         rhs: ActionViewModel<ViewModel, EventType>) -> Bool {
    return lhs.viewModel == rhs.viewModel
  }
  
  public let viewModel: ViewModel
  public let actions: AnyObserver<EventType>
  
  public init(viewModel: ViewModel, actions: AnyObserver<EventType>) {
    self.viewModel = viewModel
    self.actions = actions
  }
}

class CollapsingReusedCell<T: CollapsingItem & UIViewController & ReusedData & Eventable>:
  CollectionCell<ReusedPagerCollectionViewCell<T>> {
  public init(data: Data,
              collpasingObserver: AnyObserver<CollapsingItem>?,
              id: String = "",
              eventsEmmiter: AnyObserver<T.Event>? = nil,
              clickEvent: T.Event? = nil,
              type: CellType = .cell) {
    super.init(data: data,
               id: id,
               eventsEmmiter: eventsEmmiter,
               clickEvent: clickEvent,
               type: type,
               setup: { cell in
      collpasingObserver?.onNext(cell.viewController)
    })
  }
}

extension CoreUI {
  class Cell: CollectionViewCell {
    
    public let disposeBag = DisposeBag()
    public private(set) var reuseBag = DisposeBag()
    
    open override class var requiresConstraintBasedLayout: Bool {
      return false
    }
    
    open var shouldUseAdaptiveHitTest: Bool { return false }
    public var isNeedShowHighlightedAnimation = false
    
    override var isHighlighted: Bool {
      didSet {
        guard isNeedShowHighlightedAnimation else { return }
        UIView.animate(withDuration: 0.3) {
          self.transform = self.isHighlighted ? .init(scaleX: 0.97, y: 0.97) : .identity
        }
      }
    }
    
    // MARK: - Lifecycle
    
    open override func setup() {
      super.setup()
    }
    
    open override func prepareForReuse() {
      super.prepareForReuse()
      reuseBag = DisposeBag()
    }
    
    open override func hitTest(_ point: CGPoint, with event: UIEvent?) -> UIView? {
      let result = super.hitTest(point, with: event)
      if shouldUseAdaptiveHitTest {
        return adaptiveHitTest(for: result, at: point, with: event)
      }
      return result
    }
  }
}
// MARK: - Private

private extension CoreUI.Cell {
  func adaptiveHitTest(for target: UIView?, at point: CGPoint, with event: UIEvent?) -> UIView? {
    guard let target = target else { return nil }
    
    if let scrollView = target as? UIScrollView {
      if scrollView.isScrollEnabled,
         scrollView.contentSize.height > scrollView.bounds.size.height ||
          scrollView.contentSize.width > scrollView.bounds.size.width {
        return scrollView
      } else {
        return nil
      }
    }
    
    if let control = target as? UIControl, control.isUserInteractionEnabled {
      return control
    }
    
    func hasActiveGestures(in view: UIView) -> Bool {
      guard view.isUserInteractionEnabled else {
        return false
      }
      guard let gestures = view.gestureRecognizers else {
        return false
      }
      
      for gesture in gestures where gesture.isEnabled {
        return true
      }
      return false
    }
    
    func hasActiveGesturesRecursiveInSuperviews(of view: UIView) -> Bool {
      if hasActiveGestures(in: view) {
        return true
      }
      
      if view == self { return false }
      if let superview = view.superview, superview != contentView, superview != self {
        return hasActiveGesturesRecursiveInSuperviews(of: superview)
      }
      return false
    }
    
    if hasActiveGesturesRecursiveInSuperviews(of: target) {
      return target
    }
    return nil
  }
}
