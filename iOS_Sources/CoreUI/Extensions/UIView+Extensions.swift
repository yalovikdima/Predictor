//
//  UIView+Extensions.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 5/13/20.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

protocol ViewReusable {
  associatedtype Data
  func setup(with data: Data)
}

extension UIView {
  func changeVisibleState(isVisible: Bool = true, isAnimated: Bool = true, completion: ((Bool) -> Void)? = nil) {
    guard isVisible && alpha == 0 || !isVisible && alpha == 1 else { return }
    guard isAnimated else {
      alpha = isVisible ? 1 : 0
      return
    }
    
    UIView.animate(withDuration: 0.3,
                   animations: {
      self.alpha = isVisible ? 1 : 0
    }, completion: completion)
  }
  
  var screenshot: UIImage { UIGraphicsImageRenderer(size: bounds.size).image { _ in drawHierarchy(in: bounds, afterScreenUpdates: true) } }
}

extension Reactive where Base: UIView & ViewReusable {
  var viewModel: Binder<Base.Data> { .init(base) { base, data in base.setup(with: data) } }
}


