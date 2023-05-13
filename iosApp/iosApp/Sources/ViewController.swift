//
//  ViewController.swift
//  iosApp
//
//  Created by Eugene Filipkov on 10.03.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

import RxSwift
import RxCocoa
import RxGesture

import Alidade
import Astrolabe

import PinLayout

import PredictorSDK

public final class ViewController: UIViewController {
  public static let loggedInKey = "is_logged_in"

	private let disposeBag = DisposeBag()
  private let accountButton: UIButton = {
    let button = UIButton(type: .roundedRect)
    button.setTitle(UserDefaults.standard.bool(forKey: ViewController.loggedInKey) ? "Logout" : "Login", for: .normal)
    return button
  }()
  private let predictorButton: UIButton = {
    let button = UIButton(type: .roundedRect)
    button.setTitle("Open Predictor", for: .normal)
    return button
  }()

	private enum Layout {
		static let contentHeight = 528.ui
	}

	public override func loadView() {
		super.loadView()

    UI.setBaseWidths([.pad: 768, .phone: 375])

    view.addSubviews(accountButton, predictorButton)
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

    accountButton.rx.tap
      .asDriver()
      .drive(onNext: { [weak self] _ in
        guard let self = self else { return }

        if !UserDefaults.standard.bool(forKey: ViewController.loggedInKey) {
          self.showFakeLogin()
        } else {
          PredictorSDK.profileService().set(state: .loggedOut)
          self.changeAccontState(state: false)
        }
      }).disposed(by: disposeBag)

    predictorButton.rx.tap
      .asDriver()
      .drive(onNext: { [weak self] _ in
        guard let self = self else { return }

        let controller = PredictorViewController()
        self.navigationController?.pushViewController(controller, animated: true)
      }).disposed(by: disposeBag)
	}

	public override func viewDidLayoutSubviews() {
		super.viewDidLayoutSubviews()

    accountButton.pin
      .top(100.ui)
      .hCenter()
      .sizeToFit()
    predictorButton.pin
      .below(of: accountButton, aligned: .center)
      .marginTop(50.ui)
      .sizeToFit()
	}

	public override var shouldAutorotate: Bool { true }
	public override var supportedInterfaceOrientations: UIInterfaceOrientationMask { .portrait }
}

private extension ViewController {
  func changeAccontState(state: Bool) {
    UserDefaults.standard.setValue(state, forKey: ViewController.loggedInKey)
    UserDefaults.standard.synchronize()
    if state {
      accountButton.setTitle("Logout", for: .normal)
    } else {
      accountButton.setTitle("Login", for: .normal)
    }
    view.setNeedsLayout()
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
}
