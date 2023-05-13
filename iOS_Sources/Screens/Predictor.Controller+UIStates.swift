//
//  Predictor.Controller+UIStates.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 7.09.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit
import predictorShared

extension SceneKMM {
  var isUIScene: Bool {
    switch self {
    case .refreshing, .emptyLoading: return false
    default: return true
    }
  }
}

internal extension Predictor.Controller {
  func setupGameUI(with model: PredictionUiGameUiKMM?, scene: SceneKMM) {
    activityIndicatorStopAnimating()
    guard let model = model else { return }

    if let dialog = model.congratulationDialog {
      setupCongratulations(model: dialog)
    }

    gameView.rx.viewModel.onNext(.init(model: model))
    addSubview(gameView, scene: scene) { [weak self] in
      self?.activeView = self?.gameView
      self?.view.setNeedsLayout()
    }
  }
  
  func setupResultUI(with model: PredictionUiResultUiKMM, scene: SceneKMM) {
    activityIndicatorStopAnimating()
    resultView.rx.viewModel.onNext(model)
    addSubview(resultView, scene: scene) { [weak self] in
      self?.activeView = self?.resultView
      self?.view.setNeedsLayout()
    }
  }
  
  func setupTooLateUI(with model: PredictionUiTooLateUiKMM, scene: SceneKMM) {
    activityIndicatorStopAnimating()
    tooLateView.rx.viewModel.onNext(.init(model: model))
    addSubview(tooLateView, scene: scene) { [weak self] in
      self?.activeView = self?.tooLateView
      self?.view.setNeedsLayout()
    }
  }
  
  func setupNotAvailableUI(with model: PredictionUiNotAvailableUiKMM, scene: SceneKMM) {
    activityIndicatorStopAnimating()
    notAvailableView.rx.viewModel.onNext(model)
    addSubview(notAvailableView, scene: scene) { [weak self] in
      self?.activeView = self?.notAvailableView
      self?.view.setNeedsLayout()
    }
  }
  
  func setupShare(with model: PredictionOutputEventSharePredictionKMM) {
    activityIndicatorStartAnimating()
    shareView = .init(frame: .init(x: 0, y: 0, width: max(375.ui, UIScreen.main.bounds.width), height: 335.ui),
                      range: model.share.range,
                      shareURL: shareURL)
    let disposeBag = DisposeBag()
    shareView?.rx.screenshot
      .asDriver()
      .drive(with: self) { owner, image in
        owner.activityIndicatorStopAnimating()
        owner.internalOutput.onNext(.share(image: image))
      }.disposed(by: disposeBag)
    shareDisposeBag = disposeBag
    shareView?.rx.viewModel.onNext(model.share)
  }
  
  func setupCongratulations(model: CongratulationDialogUiKMM) {
    congratulationsView.rx.viewModel.onNext(model)
    rootController?.view.addSubview(congratulationsView)
    view.setNeedsLayout()
    congratulationsView.changeVisibleState()
  }
  
  func removeCongratulations() {
    congratulationsView.changeVisibleState(isVisible: false) { [weak self] _ in
      self?.congratulationsView.removeFromSuperview()
    }
  }
  
  func addSubview(_ view: UIView, scene: SceneKMM, competition: (() -> Void)? = nil) {
    guard view != activeView else { return }
    
    if let activeView = activeView,
       scene.isUIScene {
      self.view.addSubview(view)
      UIView.transition(from: activeView,
                        to: view,
                        duration: 0.4,
                        options: .transitionCrossDissolve) { _ in
        activeView.removeFromSuperview()
        competition?()
      }
    } else {
      UIView.transition(with: self.view, duration: 0.4, options: .transitionCrossDissolve) {
        self.view.addSubview(view)
      } completion: { _ in
        competition?()
      }
    }
  }
  
  func activityIndicatorStartAnimating() {
    view.bringSubviewToFront(activityIndicator)
    activityIndicator.startAnimating()
  }
  
  func activityIndicatorStopAnimating() {
    activityIndicator.stopAnimating()
    view.sendSubviewToBack(activityIndicator)
  }
}
