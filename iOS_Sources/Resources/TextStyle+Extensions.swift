//
//  TextStyle+Extensions.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 6.10.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit
import predictorShared

extension TextStyleKMM: Attributable {
  public var attributes: [NSAttributedString.Key : Any] { attrs as? [NSAttributedString.Key : Any] ?? [:] }
}
