//
//  Alert.Coordinator.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 3/18/20.
//  Copyright (c) 2020 Origins-Digital. All rights reserved.
//
//

import UIKit

extension Activity {
  final class Coordinator: NocturnalCoordinator {
    public typealias ResultType = Void
    
    private let image: UIImage
    private let rootController: UIViewController
    
    public init(rootController: UIViewController, image: UIImage) {
      self.rootController = rootController
      self.image = image
    }
    
    public func start() -> Observable<ResultType> {
      let controller = PredictorActivityViewController(activityItems: [image], applicationActivities: nil)
      controller.popoverPresentationController?.sourceView = rootController.view
      controller.popoverPresentationController?.sourceRect = CGRect(x: rootController.view.bounds.midX,
                                                                    y: rootController.view.bounds.midY,
                                                                    width: 0,
                                                                    height: 0)
      rootController.present(controller, animated: true)
      
      return controller.rx.deallocated
    }
  }
}

final class PredictorActivityViewController: UIActivityViewController {
  override var supportedInterfaceOrientations: UIInterfaceOrientationMask { .portrait }
  override var shouldAutorotate: Bool { false }
}
