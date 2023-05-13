//
//  UIColor+Mode.swift
//  PredictorSDK
//
//  Created by Sergei Mikhan on 20.08.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit
import Foundation

infix operator |: AdditionPrecedence
extension UIColor {
  
  /// Easily define two colors for both light and dark mode.
  /// - Parameters:
  ///   - lightMode: The color to use in light mode.
  ///   - darkMode: The color to use in dark mode.
  /// - Returns: A dynamic color that uses both given colors respectively for the given user interface style.
  static func | (lightMode: UIColor, darkMode: UIColor) -> UIColor {
    guard #available(iOS 13.0, *) else { return lightMode }
    
    return UIColor { (traitCollection) -> UIColor in
      // I don't know why traitCollection.userInterfaceStyle return incorrect value after UIActivityViewController showing and animatioin ending
      // https://origins-digital.atlassian.net/browse/PRED-394
      UIScreen.main.traitCollection.userInterfaceStyle == .light ? lightMode : darkMode
    }
  }
}


