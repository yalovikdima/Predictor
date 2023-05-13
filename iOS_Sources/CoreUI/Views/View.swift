//
//  View.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 4/20/20.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

extension CoreUI {
  class View: UIView {
    public let disposeBag = DisposeBag()
    
    public var viewStyle: ViewStyle.Predictor.View?
    public var isNeedApplyViewStyle = true
    
    // MARK: - Init
    
    public convenience init() {
      self.init(frame: CGRect.zero)
    }
    
    public required override init(frame: CGRect) {
      super.init(frame: frame)
      setup()
    }
    
    public required init?(coder aDecoder: NSCoder) {
      super.init(coder: aDecoder)
      setup()
    }
    
    // MARK: - Properties
    
    open override class var requiresConstraintBasedLayout: Bool {
      return false
    }
    
    // MARK: - Lifecycle
    
    open override func awakeFromNib() {
      super.awakeFromNib()
      setup()
    }
    
    override func traitCollectionDidChange(_ previousTraitCollection: UITraitCollection?) {
      super.traitCollectionDidChange(previousTraitCollection)
      if #available(iOS 13.0, *) {
        if (traitCollection.hasDifferentColorAppearance(comparedTo: previousTraitCollection)) {
          viewStyle?.apply(self)
        }
      }
    }
    
    override func layoutSubviews() {
      super.layoutSubviews()
      
      if isNeedApplyViewStyle {
        viewStyle?.apply(self)
      }
    }
    
    open func setup() {}
  }
}
