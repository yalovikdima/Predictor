//
//  Predictor.AnswerStatusView.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 4.02.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

import predictorShared

extension Predictor.AnswerStatus {
  final class View: CoreUI.View, ViewReusable {
    public typealias Data = PredictionUiResultUiKMM

    private let imageView = ImageView {
      $0.contentMode = .scaleAspectFill
    }
    
    public override func setup() {
      super.setup()
      
      addSubview(imageView)
    }
    
    public override func layoutSubviews() {
      super.layoutSubviews()
      
      imageView.pin.all()
    }

    public func setup(with model: Data) {
      imageView.image = model.image
    }
  }
}
