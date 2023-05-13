//
//  ProfileService.swift
//  PredictorSDK
//
//  Created by Sergei Mikhan on 1.11.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import Foundation
import predictorShared

final class InternalProfileService: ProfileService {
  static let service = InternalProfileService()
  
  internal let state = BehaviorSubject<ProfileState?>(value: nil)
  internal var profileRequiredClosure: (() -> ())? = nil
  
  func set(state: ProfileState) {
    self.state.onNext(state)
  }
  
  func register(profileRequiredClosure: @escaping (() -> ())) {
    self.profileRequiredClosure = profileRequiredClosure
  }
}
