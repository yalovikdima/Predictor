//
//  ItemEvents.swift
//  RxCocoa
//
//  Created by Krunoslav Zaher on 6/20/15.
//  Copyright © 2015 Krunoslav Zaher. All rights reserved.
//

#if os(iOS) || os(tvOS)
import UIKit

typealias ItemMovedEvent = (sourceIndex: IndexPath, destinationIndex: IndexPath)
typealias WillDisplayCellEvent = (cell: UITableViewCell, indexPath: IndexPath)
typealias DidEndDisplayingCellEvent = (cell: UITableViewCell, indexPath: IndexPath)
#endif
