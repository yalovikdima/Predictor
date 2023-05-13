//
//  Predictor.Share.View.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 19.02.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//
//

import UIKit

extension Predictor.Share {
  final class View: CoreUI.View, ViewReusable {
    private struct LayoutState: OptionSet {
      public static let stadium = LayoutState(rawValue: 1 << 0)
      public static let date = LayoutState(rawValue: 1 << 1)
      
      public let rawValue: Int
      
      public init(rawValue: Int) {
        self.rawValue = rawValue
      }
      
      public init(stadium: String?, date: String?) {
        var states: LayoutState = []
        
        if let stadium = stadium,
           !stadium.isEmpty {
          states.insert(.stadium)
        }
        if let date = date,
           !date.isEmpty {
          states.insert(.date)
        }
        self = states
      }
    }
    public struct ViewModel {
      public struct Team {
        public let name: NSAttributedString
        public let logoURL: URL?
        public let score: String?
        
        public var scoreString: NSAttributedString? {
          guard let score = score else { return nil }
          
          return "\(score)".styled(as: TextStylesExportedPublic.provider.predictorScoreShare)
        }
        
        public init(name: NSAttributedString, logoURL: URL?, score: Int? = nil) {
          self.name = name
          self.logoURL = logoURL
          self.score = score.flatMap { String($0) }
        }
      }
      
      public let title: NSAttributedString?
      public let competition: NSAttributedString
      public let stadium: NSAttributedString
      public let statusOrDate: NSAttributedString
      public let homeTeam: Team
      public let awayTeam: Team
      
      public init(model: ShareUiKMM) {
        title = model.title.attributedString
        competition = model.league.attributedString
        stadium = model.stadium.attributedString
        homeTeam = .init(name: model.homeTeamName.attributedString,
                         logoURL: model.homeTeamLogo.url,
                         score: Int(model.score.homeTeamScore.text))
        awayTeam = .init(name: model.awayTeamName.attributedString,
                         logoURL: model.awayTeamLogo.url,
                         score: Int(model.score.awayTeamScore.text))
        
        if let date = model.date {
          statusOrDate = date.attributedString +
          "\n".styled(as: TextStylesExportedPublic.provider.predictorShareDate) +
          model.time.attributedString
        } else {
          statusOrDate = StringExportedPublic.provider.getPredictorShareStatusFinal().styled(with: TextStylesExportedPublic.provider.predictorShareDate)
        }
      }
    }
    
    private let backgroundImageView = ImageView {
      $0.contentMode = .scaleAspectFill
      $0.image = ImageExportedPublic.provider.predictorShareBkg.image
    }
    private let leageuLogoImageView = ImageView {
      $0.contentMode = .scaleAspectFit
      $0.image = ImageExportedPublic.provider.predictorShareAppLogo.image
    }
    private let titleLabel = CLabel {
      $0.numberOfLines = 2			
    }
    private let competitionLabel = CLabel {
      $0.numberOfLines = 1
    }
    private let stadiumLabel = CLabel {
      $0.numberOfLines = 2
    }
    private let scoreView = Predictor.Score.View()
    private let statusOrDateLabel = CLabel {
      $0.numberOfLines = 2
    }
    private let homeTeamLogoImageView = ImageView {
      $0.contentMode = .scaleAspectFit
    }
    private let awayTeamLogoImageView = ImageView {
      $0.contentMode = .scaleAspectFit
    }
    private let homeTeamNameLabel = CLabel {
      $0.numberOfLines = 0
    }
    private let awayTeamNameLabel = CLabel {
      $0.numberOfLines = 0
    }
    private let appstoreImageView = ImageView {
      $0.image = UIImage(named: "AppStore", in: resourcesBundle(), compatibleWith: nil)
      $0.contentMode = .scaleAspectFill
    }
    private let homeSelectScoreView = Predictor.SelectScore.View()
    private let awaySelectScoreView = Predictor.SelectScore.View()
    private let qrView = QRView()
    private let range: RangeUiKMM
    private let shareURL: URL?
    
    fileprivate let screenshotCompleate = PublishSubject<UIImage>()
    
    private var reusedDisposeBag = DisposeBag()
    private var uiStates: LayoutState = []
    
    public init(frame: CGRect, range: RangeUiKMM, shareURL: URL?) {
      self.range = range
      self.shareURL = shareURL
      
      super.init(frame: frame)
    }
    
    required init?(coder aDecoder: NSCoder) {
      fatalError("init(coder:) has not been implemented")
    }
    
    required init(frame: CGRect) {
      fatalError("init(frame:) has not been implemented")
    }
    
    public override func setup() {
      super.setup()
      
      backgroundColor = ColorsExportedPublic.provider.predictorShareNumberPickerBkg.color
      
      addSubviews(backgroundImageView, leageuLogoImageView, titleLabel,
                  competitionLabel, stadiumLabel, scoreView,
                  statusOrDateLabel, homeTeamLogoImageView, awayTeamLogoImageView,
                  homeTeamNameLabel, awayTeamNameLabel, appstoreImageView,
                  homeSelectScoreView, awaySelectScoreView, qrView)
    }
    
    public override func layoutSubviews() {
      super.layoutSubviews()
      
      backgroundImageView.pin.all()
      leageuLogoImageView.pin.hCenter().top(6.ui).size(36.ui)
      titleLabel.pin.below(of: leageuLogoImageView).marginTop(4.ui).horizontally(67.ui).sizeToFit(.width)
      competitionLabel.pin.below(of: titleLabel).marginTop(1.ui).horizontally(67.ui).sizeToFit(.width)
      stadiumLabel.pin.below(of: competitionLabel).marginTop(1.ui).horizontally(67.ui).sizeToFit(.width)
      scoreView.pin.below(of: stadiumLabel, aligned: .center)
        .marginTop(uiStates.contains([.stadium, .date]) ? 0.ui : (uiStates.contains([.stadium]) ? 25.ui : 30.ui))
        .sizeToFit()
      statusOrDateLabel.pin.below(of: scoreView, aligned: .center).marginTop(1.ui).minWidth(100.ui).sizeToFit(.width)
      homeTeamLogoImageView.pin.vCenter(to: scoreView.edge.vCenter).start(45.ui).size(50.ui)
      awayTeamLogoImageView.pin.vCenter(to: scoreView.edge.vCenter).end(45.ui).size(50.ui)
      homeTeamNameLabel.pin.below(of: homeTeamLogoImageView, aligned: .center).marginTop(16.ui).width(80.ui).sizeToFit(.width)
      awayTeamNameLabel.pin.below(of: awayTeamLogoImageView, aligned: .center).marginTop(16.ui).width(80.ui).sizeToFit(.width)
      homeSelectScoreView.pin.start().vertically(21.ui).width(50.ui)
      awaySelectScoreView.pin.end().vertically(21.ui).width(50.ui)
      appstoreImageView.pin.end(to: backgroundImageView.edge.hCenter).marginEnd(5.ui).bottom(40.ui).width(114.ui).aspectRatio()
      qrView.pin.start(to: backgroundImageView.edge.hCenter).marginStart(5.ui).bottom(7.ui).size(95.ui)
    }
    
    public typealias Data = ShareUiKMM
    public func setup(with data: Data) {
      var statusOrDate: NSAttributedString? = StringExportedPublic.provider.getPredictorShareStatusFinal()
        .styled(with: TextStylesExportedPublic.provider.predictorShareDate)
      if let date = data.date {
        statusOrDate = date.attributedString.addNewLine(string: data.time.attributedString)
      }
      
      titleLabel.attributedText = data.title.attributedString
      competitionLabel.attributedText = data.league.attributedString
      stadiumLabel.attributedText = data.stadium.attributedString
      statusOrDateLabel.attributedText = statusOrDate
      homeTeamNameLabel.attributedText = data.homeTeamName.attributedString
      awayTeamNameLabel.attributedText = data.awayTeamName.attributedString
      titleLabel.lineBreakMode = .byTruncatingTail
      competitionLabel.lineBreakMode = .byTruncatingTail
      stadiumLabel.lineBreakMode = .byTruncatingTail
      scoreView.rx.viewModel.onNext(.init(model: data.score))
      homeSelectScoreView.rx.viewModel.onNext(.init(range: data.range,
                                                    selectorBackgoundColor: data.selectNumberBackground,
                                                    preselected: data.score.homeTeamScore,
                                                    style: .share))
      awaySelectScoreView.rx.viewModel.onNext(.init(range: data.range,
                                                    selectorBackgoundColor: data.selectNumberBackground,
                                                    side: .right,
                                                    preselected: data.score.awayTeamScore,
                                                    style: .share))
      qrView.rx.viewModel.onNext(.init(url: shareURL))
      uiStates = .init(stadium: data.stadium.text, date: data.league.text)
      
      setNeedsLayout()
      layoutIfNeeded()
      
      let home = homeTeamLogoImageView.setupImageRx(with: data.homeTeamLogo.url)
        .catchAndReturn(.init(container: .init(image: .init())))
        .map { $0.image }
      let away = awayTeamLogoImageView.setupImageRx(with: data.awayTeamLogo.url)
        .catchAndReturn(.init(container: .init(image: .init())))
        .map { $0.image }
      let disposeBag = DisposeBag()
      Observable.zip(home.asObservable(),
                     away.asObservable(),
                     homeSelectScoreView.rx.scrollCompleate,
                     awaySelectScoreView.rx.scrollCompleate)
        .withUnretained(self)
        .map { owner, _ in return owner.screenshot }
        .bind(to: screenshotCompleate).disposed(by: disposeBag)
      reusedDisposeBag = disposeBag
    }
  }
}

extension Reactive where Base: Predictor.Share.View {
  var screenshot: ControlEvent<UIImage> { ControlEvent(events: base.screenshotCompleate) }
}
