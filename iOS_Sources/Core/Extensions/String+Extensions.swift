//
//  String+Extensions.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 15.09.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import Foundation

extension String {
  var percentEncoding: String? {
    var characterSet = CharacterSet()
    characterSet.formUnion(.urlQueryAllowed)
    
    return self.addingPercentEncoding(withAllowedCharacters: characterSet)
  }
  
  var url: URL? { self.percentEncoding.flatMap { URL(string: $0) } }
}

extension Optional where Wrapped == String {
  var url: URL? { self.flatMap { $0.percentEncoding }.flatMap { URL(string: $0) } }
}
