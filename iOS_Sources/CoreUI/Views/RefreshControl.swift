//
//  RefreshControl.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 4/20/20.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

class RefreshControl: UIRefreshControl {
  open var adjustPosition = CGPoint(x: 0.0, y: 0.0) {
    didSet {
      setNeedsLayout()
    }
  }
  open var color: UIColor? {
    didSet {
      tintColor = color
      setNeedsLayout()
    }
  }
  
  open override func layoutSubviews() {
    super.layoutSubviews()
    
    subviews.forEach {
      $0.transform = CGAffineTransform(translationX: adjustPosition.x, y: adjustPosition.y)
    }
  }
}
