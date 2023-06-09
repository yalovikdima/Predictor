//
//  BehaviorRelay+Driver.swift
//  RxCocoa
//
//  Created by Krunoslav Zaher on 10/7/17.
//  Copyright © 2017 Krunoslav Zaher. All rights reserved.
//




extension BehaviorRelay {
    /// Converts `BehaviorRelay` to `Driver`.
    ///
    /// - returns: Observable sequence.
    func asDriver() -> Driver<Element> {
        let source = self.asObservable()
            .observe(on:DriverSharingStrategy.scheduler)
        return SharedSequence(source)
    }
}
