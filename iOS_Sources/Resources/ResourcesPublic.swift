//
//  ResourcesPublic.swift
//  PredictorSDK
//
//  Created by Sergei Mikhan on 23.08.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

import predictorShared

class ColorsExportedPublic {
  public static let provider: ColorResourceProvider = ColorResourceProviderImpl()
}

class TextStylesExportedPublic {
  public static let provider: TextStyleResourceProvider = TextStyleResourceProviderImpl()
}

class StringExportedPublic {
  public static let provider: StringResourceProvider = StringResourceProviderImpl(provider: NativeStringProviderImpl())
}

class ImageExportedPublic {
  public static let provider: ImageResourceProvider = ImageResourceProviderImpl(provider: NativeImageProviderImpl())
}
