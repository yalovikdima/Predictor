//
//  Int64+Extensions.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 27.08.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import Foundation

extension Int64 {
  var date: Date { Date(timeIntervalSince1970: TimeInterval(self) / 1000) }
}
