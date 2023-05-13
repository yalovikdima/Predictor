//
//  NSAttributedString+Extensions.swift
//  PredictorSDK
//
//  Created by Eugen Filipkov on 2/6/20.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

func + (left: NSAttributedString?, right: NSAttributedString?) -> NSAttributedString {
  let result = NSMutableAttributedString()
  if let left = left {
    result.append(left)
  }
  if let right = right {
    result.append(right)
  }
  return result
}

extension NSAttributedString {
  func trimmingCharacterSet(charSet: CharacterSet) -> NSAttributedString {
    let modifiedString = NSMutableAttributedString(attributedString: self)
    modifiedString.trimCharactersInSet(charSet: charSet)
    return NSAttributedString(attributedString: modifiedString)
  }
  
  func addAttributes(_ attributes: [NSAttributedString.Key: Any]) -> NSAttributedString {
    let mutable = NSMutableAttributedString(attributedString: self)
    let stringRange = NSRange(location: 0, length: self.string.length)
    let range: NSRangePointer? = nil
    let originalAttributes = self.attributes(at: 0, effectiveRange: range)
    originalAttributes.forEach { mutable.removeAttribute($0.key, range: stringRange) }
    let allAttributes = originalAttributes.merging(attributes) { (_, new) in new }
    mutable.addAttributes(allAttributes, range: stringRange)
    
    return NSAttributedString(attributedString: mutable)
  }
  
  func addNewLine(string: NSAttributedString?) -> NSAttributedString {
    let result = NSMutableAttributedString()
    result.append(self)
    result.append(NSAttributedString(string: "\n"))
    if let string = string {
      result.append(string)
    }
    return result
  }
}

extension NSMutableAttributedString {
  func trimCharactersInSet(charSet: CharacterSet) {
    var range = (string as NSString).rangeOfCharacter(from: charSet as CharacterSet)
    while range.length != 0 && range.location == 0 {
      replaceCharacters(in: range, with: "")
      range = (string as NSString).rangeOfCharacter(from: charSet)
    }
    range = (string as NSString).rangeOfCharacter(from: charSet, options: .backwards)
    while range.length != 0 && NSMaxRange(range) == length {
      replaceCharacters(in: range, with: "")
      range = (string as NSString).rangeOfCharacter(from: charSet, options: .backwards)
    }
  }
}
