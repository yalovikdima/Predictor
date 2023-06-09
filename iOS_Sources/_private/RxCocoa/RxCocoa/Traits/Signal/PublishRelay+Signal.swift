//
//  PublishRelay+Signal.swift
//  RxCocoa
//
//  Created by Krunoslav Zaher on 12/28/15.
//  Copyright © 2017 Krunoslav Zaher. All rights reserved.
//




extension PublishRelay {
    /// Converts `PublishRelay` to `Signal`.
    ///
    /// - returns: Observable sequence.
    func asSignal() -> Signal<Element> {
        let source = self.asObservable()
            .observe(on:SignalSharingStrategy.scheduler)
        return SharedSequence(source)
    }
}
