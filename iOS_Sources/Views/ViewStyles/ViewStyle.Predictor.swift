//
//  ViewStyle.Home.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 4/2/20.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit
import predictorShared

extension ViewStyle.Predictor.Button: Decorable {
  typealias View = UIButton
  var decoration: Decoration<View> {
    switch self {
    case let .normal(color):
      return {
        $0.backgroundColor = color
        $0.clipsToBounds = true
        $0.layer.cornerRadius = 10.ui
        $0.layer.borderWidth = 0.ui
        $0.layer.borderColor = UIColor.clear.cgColor
      }
    case let .border(borderColor):
      return {
        $0.backgroundColor = .clear
        $0.clipsToBounds = true
        $0.layer.cornerRadius = 10.ui
        $0.layer.borderWidth = 2.ui
        $0.layer.borderColor = borderColor.cgColor
      }
    case .green:
      return {
        $0.layer.borderColor = UIColor.clear.cgColor
        $0.backgroundColor = ColorsExportedPublic.provider.predictorModifiedButtonBkg.color
      }
    }
  }
}

extension ViewStyle.Predictor.View: Decorable {
  typealias View = UIView
  var decoration: Decoration<View> {
    switch self {
    case let .scoreSelector(side, color):
      return {
        $0.clipsToBounds = true
        $0.layer.cornerRadius = $0.bounds.height / 2
        $0.layer.maskedCorners = side == .left ? [.layerMaxXMinYCorner, .layerMaxXMaxYCorner] : [.layerMinXMaxYCorner, .layerMinXMinYCorner]
        $0.backgroundColor = color
      }
    case let .gift(borderColor):
      return {
        $0.clipsToBounds = true
        $0.layer.cornerRadius = $0.bounds.height / 2
        $0.layer.borderColor = borderColor.cgColor
        $0.layer.borderWidth = 1.ui
        $0.backgroundColor = ColorsExportedPublic.provider.predictorGiftBkg.color
      }
    }
  }
}
