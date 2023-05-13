//
//  Predictor.ScoreView.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 19.02.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//
//

import UIKit

extension Predictor.Score {
  final class View: CoreUI.View, ViewReusable {
    public enum Style {
      case none, normal, congratulations
    }
    
    public struct ViewModel {
      public let home: NSAttributedString
      public let away: NSAttributedString
      public let dash: NSAttributedString
      public let style: Style?
      
      public init(model: ScoreUiKMM, style: Style? = nil) {
        home = model.homeTeamScore.attributedString
        away = model.awayTeamScore.attributedString
        dash = model.separator.attributedString
        self.style = style
      }
    }
    
    private let homeScoreLabel = CLabel()
    private let dashLabel = CLabel()
    private let awayScoreLabel = CLabel()
    
    fileprivate let style = BehaviorSubject<Style>(value: .normal)
    
    private var setupDisposeBag = DisposeBag()
    private var internalStyle: Style { (try? style.value()) ?? .normal }
    private var homeAttributes: [NSAttributedString.Key: Any] = [:]
    private var awayAttributes: [NSAttributedString.Key: Any] = [:]
    private var dashAttributedString = NSAttributedString()
    
    public override func setup() {
      super.setup()
      
      addSubviews(homeScoreLabel, dashLabel, awayScoreLabel)
    }
    
    public override func layoutSubviews() {
      super.layoutSubviews()
      
      performLayout()
    }
    
    public override func sizeThatFits(_ size: CGSize) -> CGSize {
      autoSizeThatFits(size, layoutClosure: performLayout)
    }
    
    private func performLayout() {
      if internalStyle == .congratulations {
        homeScoreLabel.pin.start().vertically().sizeToFit()
        dashLabel.pin.after(of: homeScoreLabel, aligned: .center).marginStart(16.ui).width(12.ui).sizeToFit(.width)
        awayScoreLabel.pin.after(of: dashLabel).marginStart(16.ui).vertically().end().sizeToFit()
      } else {
        homeScoreLabel.pin.start().vertically().width(80.ui).sizeToFit(.width)
        dashLabel.pin.after(of: homeScoreLabel, aligned: .center).marginStart(16.ui).width(12.ui).sizeToFit(.width)
        awayScoreLabel.pin.after(of: dashLabel).marginStart(16.ui).vertically().end().width(80.ui).sizeToFit(.width)
      }
    }
    
    public typealias Data = ViewModel
    public func setup(with data: Data) {
      let disposeBag = DisposeBag()
      style
        .asDriver(onErrorJustReturn: .none)
        .filter { $0 != .none }
        .distinctUntilChanged()
        .drive(with: self) { owner, _ in
          owner.setupScore(with: data)
        }.disposed(by: disposeBag)
      setupDisposeBag = disposeBag
      
      if let style = data.style {
        self.style.onNext(style)
      }
    }

    private func setupScore(with data: Data) {
      homeScoreLabel.attributedText = data.home
      dashLabel.attributedText = data.dash
      awayScoreLabel.attributedText = data.away

      homeScoreLabel.textAlignment = .right
      awayScoreLabel.textAlignment = .left
      setNeedsLayout()
    }
  }
}

extension Reactive where Base: Predictor.Score.View {
  var style: Binder<Predictor.Score.View.Style> {
    Binder(base) { view, style in
      view.style.onNext(style)
    }
  }
}
