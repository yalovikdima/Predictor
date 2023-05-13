//
//  UIButton+Extensions.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 10/21/20.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

extension UIButton {
  func setInsets(for contentPadding: UIEdgeInsets, imageTitlePadding: CGFloat) {
    self.contentEdgeInsets = UIEdgeInsets(top: contentPadding.top,
                                          left: contentPadding.left,
                                          bottom: contentPadding.bottom,
                                          right: contentPadding.right + imageTitlePadding)
    self.titleEdgeInsets = UIEdgeInsets(top: 0,
                                        left: imageTitlePadding,
                                        bottom: 0,
                                        right: -imageTitlePadding)
  }
}
