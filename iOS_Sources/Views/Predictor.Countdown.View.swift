//
//  CountdownView.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 29.01.21.
//  Copyright (c) 2021 Origins-Digital. All rights reserved.
//

import UIKit

extension Predictor.Countdown {
  final class View: CoreUI.View, ViewReusable {
    public typealias Data = TimerUiKMM?
    
    private let daysLabel = CLabel {
      $0.attributedText = "00".styled(as: TextStylesExportedPublic.provider.predictorCountdownNumber)
    }
    private let hoursLabel = CLabel {
      $0.attributedText = "00".styled(as: TextStylesExportedPublic.provider.predictorCountdownNumber)
    }
    private let minutesLabel = CLabel {
      $0.attributedText = "00".styled(as: TextStylesExportedPublic.provider.predictorCountdownNumber)
    }
    private let daysTitleLabel = CLabel {
      $0.attributedText = StringExportedPublic.provider.getPredictorTimerDays().styled(with: TextStylesExportedPublic.provider.predictorCountdownText)
    }
    private let hoursTitleLabel = CLabel {
      $0.attributedText = StringExportedPublic.provider.getPredictorTimerHours().styled(with: TextStylesExportedPublic.provider.predictorCountdownText)
    }
    private let minutesTitleLabel = CLabel {
      $0.attributedText = StringExportedPublic.provider.getPredictorTimerMinutes().styled(with: TextStylesExportedPublic.provider.predictorCountdownText)
    }
    
    public override func setup() {
      super.setup()
      
      addSubviews(daysLabel, hoursLabel, minutesLabel,
                  daysTitleLabel, hoursTitleLabel, minutesTitleLabel)
    }
    
    public override func layoutSubviews() {
      super.layoutSubviews()
      
      performLayout()
    }
    
    public override func sizeThatFits(_ size: CGSize) -> CGSize {
      autoSizeThatFits(size, layoutClosure: performLayout)
    }
    
    private func performLayout() {
      daysLabel.pin.vertically().left().sizeToFit()
      hoursLabel.pin.after(of: daysLabel).marginLeft(21.ui).vertically().sizeToFit()
      minutesLabel.pin.after(of: hoursLabel).marginLeft(21.ui).vertically().right().sizeToFit()
      daysTitleLabel.pin.below(of: daysLabel).bottom().hCenter(to: daysLabel.edge.hCenter).width(of: daysLabel).sizeToFit()
      hoursTitleLabel.pin
        .below(of: hoursLabel)
        .bottom()
        .hCenter(to: hoursLabel.edge.hCenter)
        .width(of: hoursLabel)
        .sizeToFit()
      minutesTitleLabel.pin
        .below(of: minutesLabel)
        .bottom()
        .hCenter(to: minutesLabel.edge.hCenter)
        .width(of: minutesLabel)
        .sizeToFit()
    }

    public func setup(with model: Data) {
      if let model = model,
         Int(model.minutes.value.text) ?? 0 > 0 {
        daysTitleLabel.attributedText = model.days.label.attributedString
        hoursTitleLabel.attributedText = model.hours.label.attributedString
        minutesTitleLabel.attributedText = model.minutes.label.attributedString
        daysLabel.attributedText = model.days.value.attributedString
        hoursLabel.attributedText = model.hours.value.attributedString
        minutesLabel.attributedText = model.minutes.value.attributedString

        return
      }

      daysLabel.attributedText = "00".styled(as: TextStylesExportedPublic.provider.predictorCountdownNumber)
      hoursLabel.attributedText = "00".styled(as: TextStylesExportedPublic.provider.predictorCountdownNumber)
      minutesLabel.attributedText = "00".styled(as: TextStylesExportedPublic.provider.predictorCountdownNumber)
    }
  }
}
