//
//  Predictor.NotAvailable.View.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 22.09.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

import predictorShared

extension Predictor.NotAvailable {
  final class View: CoreUI.View, ViewReusable {
    public typealias Data = PredictionUiNotAvailableUiKMM

    private let titleLabel = CLabel { $0.numberOfLines = 2 }
    private let descriptionLabel = CLabel { $0.numberOfLines = 2 }
    private let imageView = ImageView {
      $0.contentMode = .scaleAspectFit
    }
    private let homeSelectScoreView = Predictor.SelectScore.View()
    private let awaySelectScoreView = Predictor.SelectScore.View()
    
    public override func setup() {
      super.setup()
      
      addSubviews(titleLabel, descriptionLabel, imageView,
                  homeSelectScoreView, awaySelectScoreView)
    }
    
    public override func layoutSubviews() {
      super.layoutSubviews()
      
      titleLabel.pin.top(25.ui).horizontally(35.ui).sizeToFit(.width)
      descriptionLabel.pin.below(of: titleLabel).marginTop(5.ui).horizontally(25.ui).sizeToFit(.width)
      imageView.pin.below(of: descriptionLabel, aligned: .center).marginTop(57.ui).width(187.ui).aspectRatio()
      homeSelectScoreView.pin.start().top().marginTop(91.ui).bottom(48.ui).width(50.ui)
      awaySelectScoreView.pin.end().top().marginTop(91.ui).bottom(48.ui).width(50.ui)
    }

    public func setup(with data: Data) {
      titleLabel.attributedText = data.title.attributedString
      descriptionLabel.attributedText = data.subTitle.attributedString
      imageView.image = data.image
      homeSelectScoreView.rx.viewModel.onNext(.init(range: data.range,
                                                    selectorBackgoundColor: data.numberPikerSelectBackground,
                                                    style: .disabled))
      awaySelectScoreView.rx.viewModel.onNext(.init(range: data.range,
                                                    selectorBackgoundColor: data.numberPikerSelectBackground,
                                                    side: .right,
                                                    style: .disabled))

      setNeedsLayout()
    }
  }
}
