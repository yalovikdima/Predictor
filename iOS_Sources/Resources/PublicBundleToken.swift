//
//  Resources.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 3/17/20.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import Foundation

private class BundleToken {}

func resourcesBundle() -> Bundle? {
#if BUNDLED_SDK
  let bundle = Bundle(for: BundleToken.self)
#else
  let coreBundle = Bundle(for: BundleToken.self)
  guard let path = coreBundle.path(forResource: "resource_bundle", ofType: "bundle"),
        let bundle = Bundle(path: path) else {
          return nil
        }
#endif
  
  return bundle
}
