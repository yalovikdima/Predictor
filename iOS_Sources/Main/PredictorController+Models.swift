//
//  PredictorController+Models.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 27.08.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import Foundation

public struct LoginModel {
  public let id: String
  public let firstName: String?
  public let lastName: String?
  
  public init(
    id: String, firstName: String? = nil, lastName: String? = nil
  ) {
    self.id = id
    self.firstName = firstName
    self.lastName = lastName
  }
}

public struct PredictorSettings {
  public enum LogLevel {
    case none, all
  }
  public enum Environment {
    case development(key: String), staging(key: String), production(key: String)
  }
  
  public struct Share {
    public let appURL: URL?
    
    public init(appURL: String?) {
      self.appURL = appURL?.url
    }
  }

  public let environment: Environment
  public let share: Share
  public let analytics: ((AnalyticsActions) -> Void)?
  public let logLevel: LogLevel
  
  public init(environment: Environment,
              share: Share,
              analytics: ((AnalyticsActions) -> Void)?,
              logLevel: LogLevel = LogLevel.none) {
    self.environment = environment
    self.share = share
    self.analytics = analytics
    self.logLevel = logLevel
  }
}
