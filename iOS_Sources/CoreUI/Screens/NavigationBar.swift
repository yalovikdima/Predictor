//
//  NavigationBar.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 2.03.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

class NavigationBar: UINavigationBar, DisposableContainer, TransparentNavigationBar {
  public var currentHeight: CGFloat = 0
  
  open var backImage: UIImage?
  
  // MARK: - DisposableContainer
  public let disposeBag = DisposeBag()
  
  // MARK: - NavigationBarTransparency
  public let transparencySubject = PublishSubject<CGFloat>()
  
  fileprivate let backgroundView = View {
    $0.isUserInteractionEnabled = false
  }
  fileprivate var _backgroundView: View?
  
  public override init(frame: CGRect) {
    super.init(frame: frame)
    
    let backImage = self.backImage
    setupTranparent(backImage: backImage, tint: transparentBarbackgroundColor) { [weak self] value in
      self?.setupBackgroundColor(to: self?.transparentBarbackgroundColor.withAlphaComponent(value))
    }
  }
  
  public var transparentBarbackgroundColor: UIColor = .white
  
  public required init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  // MARK: - Init
  override open func layoutSubviews() {
    super.layoutSubviews()
    if let guide = _backgroundView {
      backgroundView.pin.start().end()
        .top(to: guide.edge.top)
        .height(guide.bounds.size.height)
    }
  }
  
  override open func didAddSubview(_ subview: UIView) {
    super.didAddSubview(subview)
    if type(of: subview) == backgroundViewClass() {
      _backgroundView = subview
    }
    if subview != backgroundView, let internalBackgroundView = _backgroundView {
      internalBackgroundView.addSubview(backgroundView)
    }
  }
}

// MARK: - Private
extension NavigationBar {
  func setupBackgroundColor(to color: UIColor?) {
    self.backgroundView.backgroundColor = color
  }
}
