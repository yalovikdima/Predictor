//
//  LayoutState.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 22.09.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import Foundation

struct LayoutState: OptionSet {
  public static let sponsor = LayoutState(rawValue: 1 << 0)
  public static let gift = LayoutState(rawValue: 1 << 1)
  public static let termsAndConditions = LayoutState(rawValue: 1 << 2)
  
  public let rawValue: Int
  
  public init(rawValue: Int) {
    self.rawValue = rawValue
  }
  
  public init(model: HomeAndResultUI) {
    var states: LayoutState = []
    if model.baseUi.sponsor != nil { states.insert(.sponsor) }
    if model.baseUi.gift != nil { states.insert(.gift) }
    if model.baseUi.terms != nil { states.insert(.termsAndConditions) }
    self = states
  }
}
