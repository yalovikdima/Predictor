//
//  RxSearchControllerDelegateProxy.swift
//  RxCocoa
//
//  Created by Segii Shulga on 3/17/16.
//  Copyright © 2016 Krunoslav Zaher. All rights reserved.
//

#if os(iOS)


import UIKit

extension UISearchController: HasDelegate {
    typealias Delegate = UISearchControllerDelegate
}

/// For more information take a look at `DelegateProxyType`.
class RxSearchControllerDelegateProxy
    : DelegateProxy<UISearchController, UISearchControllerDelegate>
    , DelegateProxyType 
    , UISearchControllerDelegate {

    /// Typed parent object.
    public weak private(set) var searchController: UISearchController?

    /// - parameter searchController: Parent object for delegate proxy.
    init(searchController: UISearchController) {
        self.searchController = searchController
        super.init(parentObject: searchController, delegateProxy: RxSearchControllerDelegateProxy.self)
    }
    
    // Register known implementations
    static func registerKnownImplementations() {
        self.register { RxSearchControllerDelegateProxy(searchController: $0) }
    }
}
   
#endif
