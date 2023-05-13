//
//  Predictor.TooLate.View.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 8.10.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

import predictorShared

extension Predictor.TooLate {
  final class View: CoreUI.View, ViewReusable {
    public enum Actions {
      case termsAndConditions(url: URL?), gift(url: URL?), sponsor(url: URL?)
    }
    
    public struct ViewModel: Equatable {
      public static func == (lhs: Self, rhs: Self) -> Bool { lhs.model.hashValue == rhs.model.hashValue }
      
      public let model: PredictionUiTooLateUiKMM
      public let layoutStates: LayoutState
      
      public init(model: PredictionUiTooLateUiKMM) {
        self.model = model
        self.layoutStates = LayoutState(model: model)
      }
    }

    public typealias Data = ViewModel
    
    private let titleLabel = CLabel { $0.numberOfLines = 2 }
    private let descriptionLabel = CLabel { $0.numberOfLines = 2 }
    private let homeSelectScoreView = Predictor.SelectScore.View()
    private let awaySelectScoreView = Predictor.SelectScore.View()
    private let countdownView = Predictor.Countdown.View()
    private let scoreView = Predictor.Score.View()
    private let sponsorView = Predictor.Sponsor.View()
    private let giftView = Predictor.Gift.View()
    private let sendButton = Predictor.BorderButton {
      let title = StringExportedPublic.provider.getPredictorButtonValidate()
        .styled(with: TextStylesExportedPublic.provider.predictorValidateButtonDisable, isUppercased: true)
      $0.setAttributedTitle(title, for: .normal)
      $0.isEnabled = false
    }
    private let shareButton = Predictor.BorderButton {
      let title = StringExportedPublic.provider.getPredictorButtonShare()
        .styled(with: TextStylesExportedPublic.provider.predictorShareButtonDisable, isUppercased: true)
      $0.setAttributedTitle(title, for: .normal)
      $0.isEnabled = false
    }
    private let termsAndConditionsButton = Button {
      let terms = StringExportedPublic.provider.getPredictorTextTerms().styled(with: TextStylesExportedPublic.provider.predictorTerms)
      let title = terms.addAttributes([.underlineStyle: NSUnderlineStyle.single.rawValue])
      $0.setAttributedTitle(title, for: .normal)
    }
    
    fileprivate let actions = PublishSubject<Actions>()
    
    private var score = (homeTeamScore: "0", awayTeamScore: "0")
    private var layoutStates: LayoutState = []
    private var setupDisposeBag = DisposeBag()
    private var data: PredictionUiTooLateUiKMM?
    
    public override func setup() {
      super.setup()
      
      addSubviews(titleLabel, descriptionLabel, countdownView,
                  scoreView, homeSelectScoreView, awaySelectScoreView,
                  sendButton, shareButton)
      
      setupActions()
    }
    
    public override func layoutSubviews() {
      super.layoutSubviews()
      
      var sendBelowView: UIView = scoreView
      var sendTop = 75.ui
      var countdownTop = 17.ui
      var scoreTop = 24.ui
      
      titleLabel.pin.top(25.ui).horizontally(35.ui).sizeToFit(.width)
      descriptionLabel.pin.below(of: titleLabel).marginTop(5.ui).horizontally(25.ui).sizeToFit(.width)
      homeSelectScoreView.pin.start().top().marginTop(91.ui).bottom(48.ui).width(50.ui)
      awaySelectScoreView.pin.end().top().marginTop(91.ui).bottom(48.ui).width(50.ui)
      
      if layoutStates.contains([.gift, .sponsor]) || layoutStates.contains(.gift) {
        countdownTop = 9.ui
        scoreTop = 5.ui
      } else if layoutStates.contains(.sponsor) {
        countdownTop = 15.ui
      }
      countdownView.pin.below(of: descriptionLabel, aligned: .center).marginTop(countdownTop).sizeToFit()
      scoreView.pin.below(of: countdownView, aligned: .center).marginTop(scoreTop).sizeToFit()
      
      if layoutStates.contains([.gift, .sponsor]) {
        sendBelowView = giftView
        sendTop = 13.ui
        sponsorView.pin.below(of: scoreView, aligned: .center).marginTop(3.ui).sizeToFit()
        giftView.pin.below(of: sponsorView).marginTop(-5.ui).hCenter().width(226.ui).height(82.ui)
      } else if layoutStates.contains(.gift) {
        sendBelowView = giftView
        sendTop = 28.ui
        giftView.pin.below(of: scoreView).marginTop(5.ui).hCenter().width(226.ui).height(82.ui)
      } else if layoutStates.contains(.sponsor) {
        sendBelowView = sponsorView
        sendTop = 13.ui
        sponsorView.pin.below(of: scoreView, aligned: .center).marginTop(3.ui).sizeToFit()
      }
      sendButton.pin.below(of: sendBelowView).marginTop(sendTop).horizontally(80.ui).height(40.ui)
      shareButton.pin.below(of: sendButton).marginTop(13.ui).horizontally(80.ui).height(40.ui)
      
      if layoutStates.contains(.termsAndConditions) {
        termsAndConditionsButton.pin.bottom(3.5%).horizontally(80.ui).height(44.ui)
      }

      if let color = data?.validateButton.background {
        sendButton.viewStyle = ViewStyle.Predictor.Button.normal(color: color)
      }
      if let borderColor = data?.baseUi.shareButton?.border {
        shareButton.viewStyle = ViewStyle.Predictor.Button.border(borderColor: borderColor)
      }
    }

    public func setup(with data: Data) {
      self.data = data.model
      layoutStates = data.layoutStates
      
      setupAdditionalViews(model: data.model)
      
      titleLabel.attributedText = data.model.baseUi.title.attributedString
      descriptionLabel.attributedText = data.model.baseUi.subTitle.attributedString
      countdownView.rx.viewModel.onNext(data.model.timer)
      let homeScore = data.model.baseUi.score.homeTeamScore
      let awayScore = data.model.baseUi.score.awayTeamScore
      homeSelectScoreView.rx.viewModel.onNext(.init(range: data.model.range,
                                                    selectorBackgoundColor: data.model.numberPikerSelectBackground,
                                                    preselected: homeScore,
                                                    style: .disabled))
      awaySelectScoreView.rx.viewModel.onNext(.init(range: data.model.range,
                                                    selectorBackgoundColor: data.model.numberPikerSelectBackground,
                                                    side: .right,
                                                    preselected: awayScore,
                                                    style: .disabled))
      scoreView.rx.viewModel.onNext(.init(model: data.model.baseUi.score))
      if let sponsor = data.model.baseUi.sponsor {
        sponsorView.rx.viewModel.onNext(sponsor)
      }
      if let gift = data.model.baseUi.gift {
        giftView.rx.viewModel.onNext(gift)
      }
      sendButton.setAttributedTitle(data.model.validateButton.label.attributedString, for: .normal)
      shareButton.setAttributedTitle(data.model.baseUi.shareButton?.label.attributedString, for: .normal)
      let title = data.model.baseUi.terms?.text.attributedString.addAttributes([.underlineStyle: NSUnderlineStyle.single.rawValue])
      termsAndConditionsButton.setAttributedTitle(title, for: .normal)

      setupActions(with: data.model)
      
      setNeedsLayout()
    }
    
    private func setupAdditionalViews(model: PredictionUiTooLateUiKMM) {
      !subviews.contains(giftView) ? (model.baseUi.gift != nil  ? addSubview(giftView) : giftView.removeFromSuperview()) : nil
      !subviews.contains(sponsorView) ? (model.baseUi.sponsor != nil ? addSubview(sponsorView) : sponsorView.removeFromSuperview()) : nil
      !subviews.contains(termsAndConditionsButton) ?
      (model.baseUi.terms != nil ? addSubview(termsAndConditionsButton) : termsAndConditionsButton.removeFromSuperview()) : nil
    }
    
    private func setupActions() {
      sponsorView.rx.open
        .map { .sponsor(url: $0) }
        .bind(to: actions)
        .disposed(by: disposeBag)
    }
    
    private func setupActions(with model: PredictionUiTooLateUiKMM) {
      let disposeBag = DisposeBag()
      termsAndConditionsButton.rx.tap
        .map { .termsAndConditions(url: model.baseUi.terms?.url.url) }
        .bind(to: actions)
        .disposed(by: disposeBag)
      giftView.rx.open
        .map { .gift(url: model.baseUi.gift?.redirectUrl.url) }
        .bind(to: actions)
        .disposed(by: disposeBag)
      setupDisposeBag = disposeBag
    }
  }
}

extension Reactive where Base: Predictor.TooLate.View {
  var action: ControlEvent<Predictor.TooLate.View.Actions> {
    ControlEvent(events: base.actions)
  }
}
