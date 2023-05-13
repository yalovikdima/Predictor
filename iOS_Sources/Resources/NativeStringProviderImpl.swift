//
//  NativeStringProviderImpl.swift
//  PredictorSDK
//
//  Created by Sergei Mikhan on 12.05.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit
import predictorShared

extension String {
  
  static func tra(_ table: String, _ key: String, _ args: [CVarArg]) -> String {
    guard let localizationBundle = resourcesBundle() else {
      return key
    }
    let format = NSLocalizedString(key, tableName: table, bundle: localizationBundle, comment: "")
    return String(format: format, locale: Locale.current, arguments: args)
  }
}

class NativeStringProviderImpl: NativeStringProvider {
  func getString(by id: String, args: String...) -> String {
    let cStrs = args.compactMap { ($0 as NSString).utf8String }
    return String.tra("Localizable", id, cStrs)
  }
}
