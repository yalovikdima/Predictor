//
//  RxSearchBarDelegateProxy.swift
//  RxCocoa
//
//  Created by Krunoslav Zaher on 7/4/15.
//  Copyright © 2015 Krunoslav Zaher. All rights reserved.
//

#if os(iOS) || os(tvOS)

import UIKit


extension UISearchBar: HasDelegate {
    typealias Delegate = UISearchBarDelegate
}

/// For more information take a look at `DelegateProxyType`.
class RxSearchBarDelegateProxy
    : DelegateProxy<UISearchBar, UISearchBarDelegate>
    , DelegateProxyType 
    , UISearchBarDelegate {

    /// Typed parent object.
    public weak private(set) var searchBar: UISearchBar?

    /// - parameter searchBar: Parent object for delegate proxy.
    init(searchBar: ParentObject) {
        self.searchBar = searchBar
        super.init(parentObject: searchBar, delegateProxy: RxSearchBarDelegateProxy.self)
    }

    // Register known implementations
    static func registerKnownImplementations() {
        self.register { RxSearchBarDelegateProxy(searchBar: $0) }
    }
}

#endif
