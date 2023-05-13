//
//  Label.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 3/31/20.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

class CLabel: UILabel {
  open var padding: UIEdgeInsets = .zero { didSet { setNeedsDisplay() } }
  
  // MARK: - Overrides
  
  open override class var requiresConstraintBasedLayout: Bool {
    return false
  }
  
  open override var intrinsicContentSize: CGSize {
    var contentSize = super.intrinsicContentSize
    contentSize.height += padding.top + padding.bottom
    contentSize.width += padding.left + padding.right
    return contentSize
  }
  
  open override func sizeThatFits(_ size: CGSize) -> CGSize {
    let paddedSize = CGSize(width: size.width - (padding.left + padding.right),
                            height: size.height - (padding.top + padding.bottom))
    var contentSize = super.sizeThatFits(paddedSize)
    contentSize.height += padding.top + padding.bottom
    contentSize.width += padding.left + padding.right
    return contentSize
  }
  
  // MARK: - Init
  
  public convenience init(padding: UIEdgeInsets) {
    self.init(frame: CGRect.zero)
    self.padding = padding
  }
  
  public override init(frame: CGRect) {
    super.init(frame: frame)
  }
  
  public required init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  // MARK: - Lifecycle
  
  open override func drawText(in rect: CGRect) {
    super.drawText(in: rect.inset(by: padding))
  }
}
