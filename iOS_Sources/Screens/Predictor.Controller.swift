//
//  Predictor.Controller.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 18.02.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//
//

import UIKit
import predictorShared

extension Predictor {
  final class Controller: StoreController<PredictionUiStateKMM, PredictionOutputEventKMM> {
    typealias ResultType = Void
    
    private let maxCachePeriod: TimeInterval = 60*60*24*7
    
    internal let settings: PredictorSettings
    
    internal weak var rootController: UIViewController?
    
    internal lazy var defaultActivityIndicator = ActivityIndicatorView(frame: .init(x: 0, y: 0, width: 40.ui, height: 40.ui))
    internal let congratulationsView = Congratulations.View { $0.alpha = 0 }
    internal let notAvailableView = Predictor.NotAvailable.View()
    internal let resultView = Predictor.Result.View()
    internal let gameView = Predictor.Game.View()
    internal let tooLateView = Predictor.TooLate.View()
    
    internal let internalOutput = PublishSubject<Predictor.OutputActions>()
    
    internal var activityIndicator : ActivityIndicatorOverriding {
      overriding?.activityIndicator ?? defaultActivityIndicator
    }
    internal var shareView: Predictor.Share.View?
    internal var shareURL: URL?
    internal var activeView: UIView?
    internal var shareDisposeBag = DisposeBag()
    internal var profileStateDisposeBag = DisposeBag()
    
    private weak var overriding: Overriding?
    
    public init(id: String,
                rootController: UIViewController,
                overriding: Overriding? = nil) {
      self.rootController = rootController
      self.settings = InitializationService.service.settings
      self.overriding = overriding
      self.shareURL = settings.share.appURL
      
      super.init()
      
      self.store = InitializationService.service.kmmService.mainDI.providePredictionStore(matchId: id)
      
      ImageCache.shared.ttl = maxCachePeriod
      
      setupPredictorNavigation()
      subscribeToProfileState()
    }
    
    required init?(coder aDecoder: NSCoder) {
      fatalError("init(coder:) has not been implemented")
    }
    
    public override func loadView() {
      super.loadView()
      
      view.backgroundColor = ColorsExportedPublic.provider.predictorSecondaryColor.color
      
      view.addSubviews(activityIndicator)
    }
    
    public override func viewDidLoad() {
      super.viewDidLoad()
      setupActions()
    }
    
    public override func viewDidLayoutSubviews() {
      super.viewDidLayoutSubviews()
      
      activityIndicator.pin.center()
      congratulationsView.pin.all()
      notAvailableView.pin.all()
      resultView.pin.all()
      gameView.pin.all()
      tooLateView.pin.all()
    }
    
    public override var preferredStatusBarStyle: UIStatusBarStyle { .default }
    
    override public func handle(action: OutputEvent) {
      switch action {
      case let .sharePrediction(model):
        setupShare(with: model)
      case .showLoginDialog:
        InternalProfileService.service.profileRequiredClosure?()
      case .hideLoginDialog:
        break
      }
    }
    
    public override func handle(state: State) {
      switch state.data {
      case let .gameUi(model):
        setupGameUI(with: model, scene: state.scene)
      case let .notAvailableUi(model):
        setupNotAvailableUI(with: model, scene: state.scene)
      case let .resultUi(model):
        setupResultUI(with: model, scene: state.scene)
      case let .tooLateUi(model):
        setupTooLateUI(with: model, scene: state.scene)
      case .none: break
      }
    }
  }
}
