//
//  Predictor.Congratulations.View.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 7.05.21.
//  Copyright (c) 2021 Origins-Digital. All rights reserved.
//

import UIKit

extension Predictor.Congratulations {
  final class View: CoreUI.View, ViewReusable {
    public typealias Data = CongratulationDialogUiKMM

    private let backgroundView = View {
      $0.backgroundColor = UIColor.black.withAlphaComponent(0.7)
    }
    private let contentView = View {
      $0.backgroundColor = ColorsExportedPublic.provider.predictorCongratsBkg.color
    }
    private let titleLabel = CLabel {
      $0.numberOfLines = 2
    }
    private let descriptionLabel = CLabel {
      $0.numberOfLines = 2
    }
    private let imageView = ImageView {
      $0.image = ImageExportedPublic.provider.predictorCongratsImage.image
    }
    private let scoreView = Predictor.Score.View()
    private let homeTeamLogoImageView = ImageView {
      $0.contentMode = .scaleAspectFit
    }
    private let awayTeamLogoImageView = ImageView {
      $0.contentMode = .scaleAspectFit
    }
    private let closeButton = Button {
      $0.setImage(ImageExportedPublic.provider.predictorCongratsIcClose.image, for: .normal)
    }
    private let confirmButton = Predictor.BorderButton {
      let title = StringExportedPublic.provider.getPredictorPopupConfirm()
        .styled(with: TextStylesExportedPublic.provider.predictorCongratsConfirmButton, isUppercased: true)
      $0.setAttributedTitle(title, for: .normal)
    }
    
    fileprivate let hide = PublishSubject<Void>()
    
    private var setupDisposeBag = DisposeBag()
    private var confirmBackground = UIColor.clear
    
    public override func setup() {
      super.setup()
      
      contentView.addSubviews(titleLabel, descriptionLabel, imageView,
                              scoreView, homeTeamLogoImageView, awayTeamLogoImageView,
                              confirmButton, closeButton)
      addSubviews(backgroundView, contentView)
      
      setupRx()
    }
    
    public override func layoutSubviews() {
      super.layoutSubviews()
      
      backgroundView.pin.all()
      contentView.pin.horizontally(30.ui).height(390.ui).vCenter()
      closeButton.pin.top().right().size(.init(44.ui))
      titleLabel.pin.top(40.ui).horizontally(28.ui).sizeToFit(.width)
      descriptionLabel.pin.below(of: titleLabel).marginTop(3.ui).horizontally(28.ui).sizeToFit(.width)
      imageView.pin.below(of: descriptionLabel, aligned: .center).marginTop(29.ui).size(.init(width: 70.ui, height: 79.ui))
      scoreView.pin.below(of: imageView, aligned: .center).marginTop(14.ui).sizeToFit()
      homeTeamLogoImageView.pin.before(of: scoreView, aligned: .center).marginEnd(19.ui).size(.init(42.ui))
      awayTeamLogoImageView.pin.after(of: scoreView, aligned: .center).marginStart(19.ui).size(.init(42.ui))
      confirmButton.pin.bottom(27.ui).horizontally(50.ui).height(45.ui)

      confirmButton.viewStyle = ViewStyle.Predictor.Button.normal(color: confirmBackground)
    }

    public func setup(with data: Data) {
      confirmBackground = data.confirmBackground
      contentView.backgroundColor = data.background
      closeButton.setImage(data.closeIcon, for: .normal)
      imageView.image = data.image
      confirmButton.setAttributedTitle(data.confirm.attributedString, for: .normal)
      titleLabel.attributedText = data.title.attributedString
      descriptionLabel.attributedText = data.subTitle.attributedString
      scoreView.rx.viewModel.onNext(.init(model: data.score, style: .congratulations))
      _ = homeTeamLogoImageView.setupImage(with: data.homeTeamLogo.url)
      _ = awayTeamLogoImageView.setupImage(with: data.awayTeamLogo.url)

      setNeedsLayout()
    }
    
    private func setupRx() {
      Observable.merge(closeButton.rx.tap.map { _ in () },
                       backgroundView.rx.tapGesture().when(.recognized).map { _ in () },
                       confirmButton.rx.tap.map { _ in () })
        .observe(on: MainScheduler.asyncInstance)
        .bind(to: hide)
        .disposed(by: disposeBag)
    }
  }
}

extension Reactive where Base: Predictor.Congratulations.View {
  var hide: ControlEvent<Void> {
    ControlEvent(events: base.hide)
  }
}
