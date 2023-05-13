//
//  Web.Coordinator.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 5/7/20.
//  Copyright © 2021 Origins-digital. All rights reserved.
//
//

import SafariServices

extension Web {
  final class Coordinator: NocturnalCoordinator {
    public enum Kind {
      case browser
      case safari
    }
    
    public typealias ResultType = Void
    
    private let rootController: UIViewController
    private let kind: Kind
    private let url: URL?
    
    public init(rootController: UIViewController, url: URL?, kind: Kind) {
      self.rootController = rootController
      self.kind = kind
      self.url = url
    }
    
    public func start() -> Observable<ResultType> {
      guard let url = url else { return .empty() }
      
      switch kind {
      case .browser:
        UIApplication.shared.open(url, options: [:], completionHandler: nil)
      case .safari:
        let controller = SFSafariViewController(url: url)
        rootController.present(controller, animated: true)
      }
      return .empty()
    }
  }
}
