//
//  RxTableViewDelegateProxy.swift
//  RxCocoa
//
//  Created by Krunoslav Zaher on 6/15/15.
//  Copyright © 2015 Krunoslav Zaher. All rights reserved.
//

#if os(iOS) || os(tvOS)

import UIKit


/// For more information take a look at `DelegateProxyType`.
class RxTableViewDelegateProxy
    : RxScrollViewDelegateProxy
    , UITableViewDelegate {

    /// Typed parent object.
    public weak private(set) var tableView: UITableView?

    /// - parameter tableView: Parent object for delegate proxy.
    init(tableView: UITableView) {
        self.tableView = tableView
        super.init(scrollView: tableView)
    }

}

#endif
