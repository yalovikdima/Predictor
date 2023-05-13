//
//  Alert.Coordinator.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 3/18/20.
//  Copyright (c) 2020 Origins-Digital. All rights reserved.
//
//

import UIKit

extension Alert {
  final class Coordinator: NocturnalCoordinator {
    public typealias ResultType = Void
    
    private let rootController: UIViewController
    private let preferredStyle: UIAlertController.Style
    private let title: String?
    private let message: String?
    private let actions: [UIAlertAction]
    private let action: Observable<ResultType>
    
    public init(rootController: UIViewController,
                preferredStyle: UIAlertController.Style = .alert,
                title: String? = nil,
                message: String? = nil,
                actions: [UIAlertAction],
                action: Observable<ResultType> = .empty()) {
      self.rootController = rootController
      self.preferredStyle = preferredStyle
      self.title = title
      self.message = message
      self.actions = actions
      self.action = action
    }
    
    public func start() -> Observable<ResultType> {
      let controller = PredictorAlertController(
        title: title,
        message: message,
        preferredStyle: preferredStyle
      )
      actions.forEach { controller.addAction($0) }
      rootController.present(controller, animated: true)
      return action
    }
  }
}

final class PredictorAlertController: UIAlertController {
  override var supportedInterfaceOrientations: UIInterfaceOrientationMask { .portrait }
  override var shouldAutorotate: Bool { false }
}
