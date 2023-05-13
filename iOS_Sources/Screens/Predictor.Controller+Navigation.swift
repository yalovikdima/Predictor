//
//  Predictor.Controller+Navigation.swift
//  PredictorSDK
//
//  Created by Sergei Mikhan on 3.11.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit
import predictorShared

extension Predictor.Controller {
  func setupPredictorNavigation() {
    self.output
      .withUnretained(self)
      .flatMap { owner, action -> Observable<ResultType> in
        guard let rootController = owner.rootController else { return .empty() }
        switch action {
        case let .share(image):
          let coordinator = Activity.Coordinator(rootController: rootController, image: image)
          return coordinator.start()
        case let .termsAndConditions(url):
          let coordinator = Web.Coordinator(rootController: rootController, url: url, kind: .safari)
          return coordinator.start()
        case let .gift(url):
          let coordinator = Web.Coordinator(rootController: rootController, url: url, kind: .safari)
          return coordinator.start()
        }
      }.subscribe().disposed(by: disposeBag)
  }
  
  func coordinateAlert(with message: String) -> Observable<ResultType> {
    guard let rootController = rootController else { return .empty() }
    
    let ok = UIAlertAction(title: StringExportedPublic.provider.getPredictorCommonOk().text, style: .cancel, handler: nil)
    let coordinator = Alert.Coordinator(rootController: rootController, message: message, actions: [ok])
    return coordinator.start()
  }
}

