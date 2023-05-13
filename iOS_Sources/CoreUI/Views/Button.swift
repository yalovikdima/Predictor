//
//  Button.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 3/17/20.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

class Button: UIButton {
  open override class var requiresConstraintBasedLayout: Bool {
    return false
  }
  
  public var shouldShowTapArea = false {
    didSet { updateTapAreaView() }
  }
  public var viewStyle: ViewStyle.Predictor.Button?
  public var isNeedApplyViewStyle = true
  public var isNeedShowHighlightedAnimation = false
  
  private lazy var tapAreaView = UIView {
    $0.isUserInteractionEnabled = false
    $0.layer.borderColor = UIColor.random.withAlphaComponent(0.5).cgColor
    $0.layer.borderWidth = 1.0
    $0.backgroundColor = .clear
  }
  
  override var isHighlighted: Bool {
    didSet {
      guard isNeedShowHighlightedAnimation else { return }
      UIView.animate(withDuration: 0.3) {
        self.transform = self.isHighlighted ? .init(scaleX: 0.97, y: 0.97) : .identity
      }
    }
  }
  // MARK: Init
  
  required public init() {
    super.init(frame: .zero)
    setup()
  }
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    setup()
  }
  
  required public init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  // MARK: - Public
  
  open func setup() { }
  
  func hitArea() -> CGRect {
    let minSize: CGFloat = 44.0
    let hitSize = CGSize(width: max(minSize, bounds.size.width),
                         height: max(minSize, bounds.size.height))
    let hitArea = CGRect(x: bounds.midX - hitSize.width * 0.5,
                         y: bounds.midY - hitSize.height * 0.5,
                         width: hitSize.width,
                         height: hitSize.height)
    return hitArea
  }
  
  // MARK: - Overrides
  
  open override func layoutSubviews() {
    super.layoutSubviews()
    
    if isNeedApplyViewStyle {
      viewStyle?.apply(self)
    }
    
    guard shouldShowTapArea else { return }
    
    tapAreaView.frame = hitArea()
  }
  
  open override func point(inside point: CGPoint, with event: UIEvent?) -> Bool {
    return hitArea().contains(point)
  }
  
  override func traitCollectionDidChange(_ previousTraitCollection: UITraitCollection?) {
    super.traitCollectionDidChange(previousTraitCollection)
    if #available(iOS 13.0, *) {
      if (traitCollection.hasDifferentColorAppearance(comparedTo: previousTraitCollection)) {
        viewStyle?.apply(self)
      }
    }
  }
}

// MARK: - Private

private extension Button {
  func updateTapAreaView() {
    if shouldShowTapArea {
      addSubview(tapAreaView)
      bringSubviewToFront(tapAreaView)
      setNeedsLayout()
    } else {
      tapAreaView.removeFromSuperview()
    }
  }
}
