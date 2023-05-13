//
//  Analytics.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 24.05.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import Foundation

public enum AnalyticsActions {
  case open(game: AnalyticsGame), validate(score: AnalyticsScore, game: AnalyticsGame), modified(score: AnalyticsScore, game: AnalyticsGame)
}

public struct AnalyticsScore {
  public let home: String
  public let away: String
}

public struct AnalyticsGame {
  public struct Team {
    public let name: String
  }
  
  public let id: String
  public let homeTeam: Team
  public let awayTeam: Team
  public let stadium: String
  public let league: String
  public let startDate: TimeInterval
  
  public init(id: String, homeTeam: AnalyticsGame.Team, awayTeam: AnalyticsGame.Team, stadium: String, league: String, startDate: Int64) {
    self.id = id
    self.homeTeam = homeTeam
    self.awayTeam = awayTeam
    self.stadium = stadium
    self.league = league
    self.startDate = TimeInterval(startDate)
  }
}
