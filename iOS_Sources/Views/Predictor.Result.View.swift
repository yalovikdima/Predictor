//
//  Predictor.Result.View.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 22.09.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

import predictorShared

extension Predictor.Result {
  final class View: CoreUI.View, ViewReusable {
    public enum Actions {
      case share, termsAndConditions(url: URL?), gift(url: URL?), sponsor(url: URL?)
    }

    public typealias Data = PredictionUiResultUiKMM

    private let titleLabel = CLabel { $0.numberOfLines = 2 }
    private let descriptionLabel = CLabel { $0.numberOfLines = 2 }
    private let scoreView = Predictor.Score.View()
    private let answerStatusView = Predictor.AnswerStatus.View()
    private let giftView = Predictor.Gift.View()
    private let sponsorView = Predictor.Sponsor.View()
    private let shareButton = Predictor.BorderButton {
      let titleNormal = StringExportedPublic.provider.getPredictorButtonShare()
        .styled(with: TextStylesExportedPublic.provider.predictorShareButton, isUppercased: true)
      let titleDisabled = StringExportedPublic.provider.getPredictorButtonShare()
        .styled(with: TextStylesExportedPublic.provider.predictorShareButtonDisable, isUppercased: true)
      $0.setAttributedTitle(titleNormal, for: .normal)
      $0.setAttributedTitle(titleDisabled, for: .disabled)
      $0.isNeedShowHighlightedAnimation = true
    }
    private let termsAndConditionsButton = Button {
      let terms = StringExportedPublic.provider.getPredictorTextTerms().styled(with: TextStylesExportedPublic.provider.predictorTerms)
      let title = terms.addAttributes([.underlineStyle: NSUnderlineStyle.single.rawValue])
      $0.setAttributedTitle(title, for: .normal)
    }
    
    fileprivate let actions = PublishSubject<Actions>()
    
    private var layoutStates: LayoutState = []
    private var setupDisposeBag = DisposeBag()
    private var data: Data?
    
    public override func setup() {
      super.setup()
      
      addSubviews(titleLabel, descriptionLabel, scoreView,
                  answerStatusView, shareButton)
      
      setupActions()
    }
    
    public override func layoutSubviews() {
      super.layoutSubviews()
      
      var answerStatusBelowView: UIView = scoreView
      var answerStatusTop = 38.ui
      var shareTop = 52.ui
      var scoreTop = 64.ui
      
      titleLabel.pin.top(25.ui).horizontally(35.ui).sizeToFit(.width)
      descriptionLabel.pin.below(of: titleLabel).marginTop(5.ui).horizontally(25.ui).sizeToFit(.width)
      
      if layoutStates.contains([.gift, .sponsor]) || layoutStates.contains(.gift) {
        scoreTop = 24.ui
      } else if layoutStates.contains(.sponsor) {
        scoreTop = 42.ui
      }
      scoreView.pin.below(of: descriptionLabel, aligned: .center).marginTop(scoreTop).sizeToFit()
      
      if layoutStates.contains([.gift, .sponsor]) {
        answerStatusBelowView = giftView
        answerStatusTop = 4.ui
        shareTop = 24.ui
        sponsorView.pin.below(of: scoreView, aligned: .center).marginTop(3.ui).sizeToFit()
        giftView.pin.below(of: sponsorView, aligned: .center).marginTop(3.ui).width(226.ui).height(82.ui)
      } else if layoutStates.contains(.gift) {
        answerStatusBelowView = giftView
        answerStatusTop = 18.ui
        shareTop = 32.ui
        giftView.pin.below(of: scoreView, aligned: .center).marginTop(5.ui).width(226.ui).height(82.ui)
      } else if layoutStates.contains(.sponsor) {
        answerStatusBelowView = sponsorView
        answerStatusTop = 29.ui
        shareTop = 35.ui
        sponsorView.pin.below(of: scoreView, aligned: .center).marginTop(3.ui).sizeToFit()
      }
      answerStatusView.pin.below(of: answerStatusBelowView, aligned: .center).marginTop(answerStatusTop).size(41.ui)
      shareButton.pin.below(of: answerStatusView).marginTop(shareTop).horizontally(80.ui).height(40.ui)
      
      if layoutStates.contains(.termsAndConditions) {
        termsAndConditionsButton.pin.bottom(4.5%).horizontally(80.ui).height(44.ui)
      }
      if let borderColor = data?.baseUi.shareButton?.border {
        shareButton.viewStyle = ViewStyle.Predictor.Button.border(borderColor: borderColor)
      }
    }

    public func setup(with data: Data) {
      self.data = data
      layoutStates = LayoutState(model: data)
      
      addAdditionalViews(model: data)
      
      titleLabel.attributedText = data.baseUi.title.attributedString
      descriptionLabel.attributedText = data.baseUi.subTitle.attributedString
      shareButton.setAttributedTitle(data.baseUi.shareButton?.label.attributedString, for: .normal)
      let title = data.baseUi.terms?.text.attributedString.addAttributes([.underlineStyle: NSUnderlineStyle.single.rawValue])
      termsAndConditionsButton.setAttributedTitle(title, for: .normal)
      scoreView.rx.viewModel.onNext(.init(model: data.baseUi.score))
      if let sponsor = data.baseUi.sponsor {
        sponsorView.rx.viewModel.onNext(sponsor)
      }
      if let gift = data.baseUi.gift {
        giftView.rx.viewModel.onNext(gift)
      }
      answerStatusView.rx.viewModel.onNext(data)
      
      scoreView.rx.style.onNext(.normal)
      setNeedsLayout()
      
      setupActions(with: data)
    }
    
    private func addAdditionalViews(model: Data) {
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
    
    private func setupActions(with model: Data) {
      let disposeBag = DisposeBag()
      shareButton.rx.tap
        .map { _ in .share }
        .bind(to: actions)
        .disposed(by: disposeBag)
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

extension Reactive where Base: Predictor.Result.View {
  var action: ControlEvent<Predictor.Result.View.Actions> {
    ControlEvent(events: base.actions)
  }
}

