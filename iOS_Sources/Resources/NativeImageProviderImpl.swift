//
//  NativeImageProviderImpl.swift
//  PredictorSDK
//
//  Created by Sergei Mikhan on 26.08.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit
import predictorShared

class NativeImageProviderImpl: NativeImageProvider {
  func getImage(by id: String) -> ImageKMM {
    guard let image = UIImage(named: id, in: resourcesBundle(), compatibleWith: nil) else {
      return .init(image: UIImage())
    }
    return .init(image: image)
  }
}
