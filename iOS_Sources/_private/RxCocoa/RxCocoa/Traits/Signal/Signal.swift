//
//  Signal.swift
//  RxCocoa
//
//  Created by Krunoslav Zaher on 9/26/16.
//  Copyright © 2016 Krunoslav Zaher. All rights reserved.
//



/**
 Trait that represents observable sequence with following properties:
 
 - it never fails
 - it delivers events on `MainScheduler.instance`
 - `share(scope: .whileConnected)` sharing strategy

 Additional explanation:
 - all observers share sequence computation resources
 - there is no replaying of sequence elements on new observer subscription
 - computation of elements is reference counted with respect to the number of observers
 - if there are no subscribers, it will release sequence computation resources

 In case trait that models state propagation is required, please check `Driver`.

 `Signal<Element>` can be considered a builder pattern for observable sequences that model imperative events part of the application.
 
 To find out more about units and how to use them, please visit `Documentation/Traits.md`.
 */
typealias Signal<Element> = SharedSequence<SignalSharingStrategy, Element>

struct SignalSharingStrategy: SharingStrategyProtocol {
    static var scheduler: SchedulerType { SharingScheduler.make() }
    
    static func share<Element>(_ source: Observable<Element>) -> Observable<Element> {
        source.share(scope: .whileConnected)
    }
}

extension SharedSequenceConvertibleType where SharingStrategy == SignalSharingStrategy {
    /// Adds `asPublisher` to `SharingSequence` with `PublishSharingStrategy`.
    func asSignal() -> Signal<Element> {
        self.asSharedSequence()
    }
}
