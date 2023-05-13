//
//  PredictorViewController.swift
//  iosApp
//
//  Created by Eugene Filipkov on 9.11.22.
//  Copyright © 2022 Origins-Digital. All rights reserved.
//

import UIKit

import PredictorSDK

import PredictorSDK_Private

import RxSwift
import RxCocoa
import RxGesture

import Alidade
import Astrolabe

import PinLayout

public final class PredictorViewController: UIViewController {
  private let imageView = UIImageView {
    $0.contentMode = .scaleAspectFill
    $0.image = UIImage(named: "field")
  }
  private let scrollView = UIScrollView {
    $0.backgroundColor = .clear
    $0.showsVerticalScrollIndicator = false
    $0.contentInsetAdjustmentBehavior = .never
  }
  private let disposeBag = DisposeBag()
  private let shareAppURL = "http://netcosports.com"
  private let key = "HkzLQ4pzu"

  private var timerDisposeBag: DisposeBag? = DisposeBag()
  private var controller: PredictorController?
  private var environment: PredictorSettings.Environment?
  private var analytics: ((AnalyticsActions) -> Void)?

  private typealias PredictorController = UIViewController

  private enum Layout {
    static let contentHeight = 528.ui
  }

  public override func loadView() {
    super.loadView()

    UI.setBaseWidths([.pad: 768, .phone: 375])

    view.addSubviews(imageView, scrollView)
  }

  public override func viewDidLoad() {
    super.viewDidLoad()

    if #available(iOS 13.0, *) {
      view.backgroundColor = UIColor(dynamicProvider: { traitCollection in
        traitCollection.userInterfaceStyle == .dark ? #colorLiteral(red: 0.08235294, green: 0.08235294, blue: 0.08235294, alpha: 1) : #colorLiteral(red: 0.972549, green: 0.972549, blue: 0.972549, alpha: 1)
      })
    } else {
      view.backgroundColor = #colorLiteral(red: 0.972549, green: 0.972549, blue: 0.972549, alpha: 1)
    }

    PredictorSDK.profileService().register { [weak self] in
      self?.showFakeLogin()
    }

    initializePredictor()
    startTimer()

    scrollView.contentSize = .init(width: view.width, height: Layout.contentHeight)
  }

  public override func viewDidLayoutSubviews() {
    super.viewDidLayoutSubviews()

    imageView.pin.top().horizontally().height(284.ui)
    scrollView.pin.below(of: imageView).horizontally().bottom()

    guard let controller = controller else { return }
    controller.view.pin.top().horizontally().height(Layout.contentHeight)
  }

  public override var shouldAutorotate: Bool { true }
  public override var supportedInterfaceOrientations: UIInterfaceOrientationMask { .portrait }

  public override func motionBegan(_ motion: UIEvent.EventSubtype, with event: UIEvent?) {
    if motion == .motionShake {
      showMenu()
    }
  }
}

private extension PredictorViewController {
  func initializePredictor() {
    analytics = {
      switch $0 {
      case .open:
        print("===== open")
      case .validate:
        print("===== validate")
      case .modified:
        print("===== modified")
      @unknown default: break
      }
    }

    var logLevel = PredictorSettings.LogLevel.none
    var environment = PredictorSettings.Environment.production(key: self.key)
  #if DEBUG
    logLevel = .all
    environment = .staging(key: self.key)
  #endif

    if let environment = self.environment {
      PredictorSDK.initialization(settings: .init(environment: environment,
                                                  share: .init(appURL: shareAppURL),
                                                  analytics: analytics,
                                                  logLevel: logLevel))
    } else {
      PredictorSDK.initialization(settings: .init(environment: environment,
                                                  share: .init(appURL: shareAppURL),
                                                  analytics: analytics,
                                                  logLevel: logLevel))
    }
  }

  func startTimer() {
    timerDisposeBag = nil

    let disposeBag = DisposeBag()
    Observable<Int>.timer(.seconds(0), period: .seconds(30), scheduler: MainScheduler.instance)
      .flatMap { [weak self] _ -> Observable<String> in
        guard let self = self else { return .empty() }

        return try self.loadEvent()
      }.distinctUntilChanged()
      .observeOn(MainScheduler.asyncInstance)
      .subscribe(onNext: { [weak self] id in
        guard let self = self else { return }

        self.setupPredictor(with: id)
      })
      .disposed(by: disposeBag)
    timerDisposeBag = disposeBag
  }

  func loadEvent() throws -> Observable<String> {
//    return .just("348239")
    #if DEBUG
    let URLString = "https://www.dl.dropboxusercontent.com/s/wxv0d0i328oqdam/predictor_event_id_dev.json"
    #else
    let URLString = "https://www.dl.dropboxusercontent.com/s/b5agc0q1fg4fe2b/predictor_event_id.json"
    #endif

    guard let url = URL(string: URLString) else { return .empty() }

    return URLSession.shared.rx.json(request: .init(url: url)).map {
      guard let json = $0 as? [String: Any], let eventId = json["event_id"] as? String else { return "" }

      return eventId
    }
  }

  func showFakeLogin() {
    let alertController = UIAlertController(title: "Login", message: nil, preferredStyle: .alert)
    alertController.addTextField { textField in
      textField.placeholder = "Device Id"
      textField.text = UIDevice.current.identifierForVendor?.uuidString
    }
    alertController.addTextField { textField in
      textField.placeholder = "First Name"
    }
    alertController.addTextField { textField in
      textField.placeholder = "Lirst Name"
    }

    let loginAction = UIAlertAction(title: "Login", style: .default) { [weak self] _ in
      let id = alertController.textFields?[0].text ?? (UIDevice.current.identifierForVendor?.uuidString ?? "1111")
      let firstName = alertController.textFields?[1].text ?? "test@gmail.com"
      let lastName = alertController.textFields?[2].text

      let model = LoginModel(id: id, firstName: firstName, lastName: lastName)
      PredictorSDK.profileService().set(state: .loggedIn(model: model))

      self?.changeAccontState(state: true)
    }
    let cancelAction = UIAlertAction(title: "Cancel", style: .cancel)

    alertController.addAction(loginAction)
    alertController.addAction(cancelAction)

    present(alertController, animated: true)
  }

  func setupPredictor(with id: String) {
    if controller != nil {
      controller?.removeFromParent()
      controller?.view.removeFromSuperview()
    }
    #if DEBUG
    controller = PredictorSDK.controller(with: id,
                                         rootController: self,
                                         overriding: nil)
    #else
    controller = PredictorSDK.controller(with: id,
                                         rootController: self)
    #endif

    if let controller = self.controller {
      scrollView.addSubview(controller.view)
      addChild(controller)
      view.setNeedsLayout()
    }
  }

  func changeAccontState(state: Bool) {
    UserDefaults.standard.setValue(state, forKey: ViewController.loggedInKey)
    UserDefaults.standard.synchronize()
  }

  func showMenu() {
    let menuController = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
    let loginMenuAction = UIAlertAction(title: "Login menu", style: .default) { [weak self] _ in
      self?.showLoginTestMenu()
    }
    let environmentMenuAction = UIAlertAction(title: "Environment menu", style: .default) { [weak self] _ in
      self?.showEnvironmentMenu()
    }
    let cancelAction = UIAlertAction(title: "Cancel", style: .cancel)
    menuController.addAction(loginMenuAction)
    menuController.addAction(environmentMenuAction)
    menuController.addAction(cancelAction)

    present(menuController, animated: true)
  }

  func showLoginTestMenu() {
    let menuController = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
    let loginAction = UIAlertAction(title: "Login", style: .default) { [weak self] _ in
      self?.showFakeLogin()
    }
    let logoutAction = UIAlertAction(title: "Logout", style: .default) { [weak self] _ in
      PredictorSDK.profileService().set(state: .loggedOut)
      self?.changeAccontState(state: false)
    }
    let cancelAction = UIAlertAction(title: "Cancel", style: .cancel)
    menuController.addAction(loginAction)
    menuController.addAction(logoutAction)
    menuController.addAction(cancelAction)

    present(menuController, animated: true)
  }

  func showEnvironmentMenu() {
    let menuController = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
    let developmentAction = UIAlertAction(title: "Development", style: .default) { [weak self] _ in
      guard let self = self else { return }

      self.environment = .development(key: self.key)
      self.initializePredictor()
      self.startTimer()
    }
    let stagingAction = UIAlertAction(title: "Staging", style: .default) { [weak self] _ in
      guard let self = self else { return }

      self.environment = .staging(key: self.key)
      self.initializePredictor()
      self.startTimer()
    }
    let productionAction = UIAlertAction(title: "Production", style: .default) { [weak self] _ in
      guard let self = self else { return }

      self.environment = .production(key: self.key)
      self.initializePredictor()
      self.startTimer()
    }
    let cancelAction = UIAlertAction(title: "Cancel", style: .cancel)
    menuController.addAction(developmentAction)
    menuController.addAction(stagingAction)
    menuController.addAction(productionAction)
    menuController.addAction(cancelAction)

    present(menuController, animated: true)
  }
}

extension PredictorViewController: Overriding {}
