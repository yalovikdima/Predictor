//
//  RxNavigationControllerDelegateProxy.swift
//  RxCocoa
//
//  Created by Diogo on 13/04/17.
//  Copyright © 2017 Krunoslav Zaher. All rights reserved.
//

#if os(iOS) || os(tvOS)

    import UIKit
    

    extension UINavigationController: HasDelegate {
        typealias Delegate = UINavigationControllerDelegate
    }

    /// For more information take a look at `DelegateProxyType`.
    class RxNavigationControllerDelegateProxy
        : DelegateProxy<UINavigationController, UINavigationControllerDelegate>
        , DelegateProxyType 
        , UINavigationControllerDelegate {

        /// Typed parent object.
        public weak private(set) var navigationController: UINavigationController?

        /// - parameter navigationController: Parent object for delegate proxy.
        init(navigationController: ParentObject) {
            self.navigationController = navigationController
            super.init(parentObject: navigationController, delegateProxy: RxNavigationControllerDelegateProxy.self)
        }

        // Register known implementations
        static func registerKnownImplementations() {
            self.register { RxNavigationControllerDelegateProxy(navigationController: $0) }
        }
    }
#endif
