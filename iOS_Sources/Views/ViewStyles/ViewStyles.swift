//
//  ViewStyles.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 4/2/20.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

enum ViewStyle {
  enum Predictor {
    enum Button {
      case border(borderColor: UIColor), normal(color: UIColor), green
    }
    enum View {
      enum Side {
        case left, right
      }
      case scoreSelector(side: Side, color: UIColor)
      case gift(borderColor: UIColor)
    }
  }
}
