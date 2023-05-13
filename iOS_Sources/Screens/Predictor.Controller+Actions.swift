//
//  Predictor.Controller+Actions.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 7.09.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit
import predictorShared

internal extension Predictor.Controller {
  func subscribeToProfileState() {
    let disposeBag = DisposeBag()
    InternalProfileService.service.state
      .compactMap { $0 }
      .asObservable()
      .observe(on: MainScheduler.instance)
      .subscribe(with: self) { owner, state in
        switch state {
        case let .loggedIn(model):
          owner.currentState?.login(contestant: .init(.init(firstName: model.firstName,
                                                            lastName: model.lastName,
                                                            externalId: model.id)))
        case .loggedOut:
          owner.gameView.resetScore()
          owner.currentState?.logout()
        }
      }.disposed(by: disposeBag)
    profileStateDisposeBag = disposeBag
  }
  
  func setupActions() {
    congratulationsView.rx.hide
      .asDriver()
      .drive(with: self) { owner, _ in
        owner.currentState?.hideCongratsDialog()
        owner.removeCongratulations()
      }.disposed(by: disposeBag)
    
    gameView.rx.action
      .asDriver()
      .drive(with: self) { owner, action in
        switch action {
        case let .send(predictionId):
          if predictionId != nil {
            owner.currentState?.onModifyClick()
          } else {
            owner.currentState?.onValidateClick()
          }
        case .share:
          owner.currentState?.onShareClick()
          owner.activityIndicatorStartAnimating()
        case let .gift(url):
          owner.internalOutput.onNext(.gift(url: url))
        case let .termsAndConditions(url):
          owner.internalOutput.onNext(.termsAndConditions(url: url))
        case let .sponsor(url):
          owner.openURL(url)
        case let .score(score):
          owner.currentState?.onScoreChange(score: score)
        }
      }.disposed(by: disposeBag)
    
    tooLateView.rx.action
      .withUnretained(self)
      .compactMap { owner, action in
        switch action {
        case let .gift(url): return .gift(url: url)
        case let .sponsor(url):
          owner.openURL(url)
          return nil
        case let .termsAndConditions(url): return .termsAndConditions(url: url)
        }
      }.bind(to: internalOutput).disposed(by: disposeBag)
    
    resultView.rx.action
      .withUnretained(self)
      .compactMap { owner, action in
        switch action {
        case .share:
          owner.currentState?.onShareClick()
          owner.activityIndicatorStartAnimating()
          return nil
        case let .gift(url):
          return .gift(url: url)
        case let .sponsor(url):
          owner.openURL(url)
          return nil
        case let .termsAndConditions(url):
          return .termsAndConditions(url: url)
        }
      }.bind(to: internalOutput).disposed(by: disposeBag)
  }
  
  func openURL(_ url: URL?) {
    if let url = url,
       UIApplication.shared.canOpenURL(url) {
      UIApplication.shared.open(url, options: [:], completionHandler: nil)
    }
  }
}
