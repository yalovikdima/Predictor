//
//  KMMExtensions.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 11.10.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

import predictorShared

extension LabelKMM: Attributable {
  var attributes: [NSAttributedString.Key : Any] { style }
}

extension LabelKMM {
  public var attributedString: NSAttributedString {
    text.styled(as: self)
  }
}

extension LabelWithIconKMMKMM {
  public var attributedString: NSAttributedString {
    label.attributedString
  }
  
  public var attributedStringWithImage: NSAttributedString {
    attributedString + NSAttributedString(attachment: NSTextAttachment { $0.image = icon })
  }
}

extension StringKMM {
  func styled(with style: Attributable, isUppercased: Bool = false) -> NSAttributedString { (isUppercased ? text.uppercased() : text).styled(as: style) }
}

extension ScoreUiKMM {
  var full: NSAttributedString {
    homeTeamScore.attributedString + separator.attributedString + awayTeamScore.attributedString
  }

  var entity: ScoreEntity {
    .init(homeScore: Int32(homeTeamScore.text) ?? 0, awayScore: Int32(awayTeamScore.text) ?? 0)
  }
}
