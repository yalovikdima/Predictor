//
//  Predictor.ContainerController.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 9.03.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

import predictorShared

public protocol Overriding: AnyObject {
  var activityIndicator: ActivityIndicatorOverriding? { get }
}

public extension Overriding {
  var activityIndicator: ActivityIndicatorOverriding? { nil }
}

public protocol ProfileService {
  func register(profileRequiredClosure: @escaping () -> ())
  func set(state: ProfileState)
}

public enum ProfileState {
  case loggedIn(model: LoginModel), loggedOut
}

public func profileService() -> ProfileService {
  return InternalProfileService.service
}

public func initialization(settings: PredictorSettings) {
  InitializationService.service.setup(settings: settings)
}

public func controller(with id: String,
                       rootController: UIViewController,
                       overriding: Overriding? = nil) -> UIViewController {
  UI.setBaseWidths([.pad: 768, .phone: 375])
  
  return Predictor.Controller(id: id,
                              rootController: rootController,
                              overriding: overriding)
}

public func isGameAvailable(id: String, completition: @escaping (Bool, Error?) -> Void) {
  InitializationService.service.kmmService.isGameAvailable(id: id, completition: completition)
}
