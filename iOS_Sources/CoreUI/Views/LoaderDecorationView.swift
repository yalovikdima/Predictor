//
//  LoaderDecorationView.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 3/31/20.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

class LoaderDecorationView: CollectionViewCell, Decorationable {
  public static var zIndex: Int { return Int.max }
  public static let kind = "LoaderDecorationView"

  let activityIndicator = ActivityIndicator()

  public override func setup() {
    super.setup()
    isUserInteractionEnabled = false
    contentView.backgroundColor = .clear
    contentView.addSubviews(activityIndicator)
  }

  public override func layoutSubviews() {
    super.layoutSubviews()

    activityIndicator.pin.size(40.ui).center()
  }

  public override func apply(_ layoutAttributes: UICollectionViewLayoutAttributes) {
    super.apply(layoutAttributes)
    if !layoutAttributes.isHidden {
      activityIndicator.startAnimating()
    } else {
      activityIndicator.stopAnimating()
    }
  }
}
