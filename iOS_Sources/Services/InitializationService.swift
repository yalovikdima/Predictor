//
//  InitializationService.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 15.09.22.
//  Copyright © 2022 Origins-Digital. All rights reserved.
//

import Foundation

import predictorShared

final class InitializationService: InitializationServicing {
  static let service = InitializationService()

  public var settings: PredictorSettings {
    guard let settings = internalSettings else { fatalError("Can't setup settings") }

    return settings
  }
  public var kmmService: KMMServicing {
    guard let kmmService = internalKmmService else { fatalError("Can't setup KMMService") }

    return kmmService
  }

  private var internalSettings: PredictorSettings?
  private var internalKmmService: KMMServicing?

  public func setup(settings: PredictorSettings) {
    weak var analytics = self

    internalSettings = settings
    internalKmmService = KMMService(config: config(), logLevel: settings.logLevel, analytics: analytics)
  }

  private func config() -> PredictorApiConfig {
    switch settings.environment {
    case let .development(key): return PredictorApiConfig.Companion().dev(accountKey: key)
    case let .staging(key): return PredictorApiConfig.Companion().staging(accountKey: key)
    case let .production(key): return PredictorApiConfig.Companion().prod(accountKey: key)
    }
  }
}

extension InitializationService: PredictorAnalytics {
  func onModified(score: ScoreEntity, game: GameEntity) {
    let action = AnalyticsActions.modified(score: .init(home: String(score.homeScore),
                                                        away: String(score.awayScore)),
                                           game: .init(id: game.id,
                                                       homeTeam: .init(name: game.homeTeamName),
                                                       awayTeam: .init(name: game.awayTeamName),
                                                       stadium: game.stadium,
                                                       league: game.league,
                                                       startDate: game.startDate))
    internalSettings?.analytics?(action)
  }

  func onPredictorOpened(game: GameEntity) {
    let action = AnalyticsActions.open(game: .init(id: game.id,
                                                   homeTeam: .init(name: game.homeTeamName),
                                                   awayTeam: .init(name: game.awayTeamName),
                                                   stadium: game.stadium,
                                                   league: game.league,
                                                   startDate: game.startDate))
    internalSettings?.analytics?(action)
  }

  func onValidated(score: ScoreEntity, game: GameEntity) {
    let action = AnalyticsActions.validate(score: .init(home: String(score.homeScore),
                                                        away: String(score.awayScore)),
                                           game: .init(id: game.id,
                                                       homeTeam: .init(name: game.homeTeamName),
                                                       awayTeam: .init(name: game.awayTeamName),
                                                       stadium: game.stadium,
                                                       league: game.league,
                                                       startDate: game.startDate))
    internalSettings?.analytics?(action)
  }
}
