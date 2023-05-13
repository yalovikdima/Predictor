//
//  RxPickerViewDelegateProxy.swift
//  RxCocoa
//
//  Created by Segii Shulga on 5/12/16.
//  Copyright © 2016 Krunoslav Zaher. All rights reserved.
//

#if os(iOS)

    
    import UIKit

    extension UIPickerView: HasDelegate {
        typealias Delegate = UIPickerViewDelegate
    }

    class RxPickerViewDelegateProxy
        : DelegateProxy<UIPickerView, UIPickerViewDelegate>
        , DelegateProxyType 
        , UIPickerViewDelegate {

        /// Typed parent object.
        public weak private(set) var pickerView: UIPickerView?

        /// - parameter pickerView: Parent object for delegate proxy.
        init(pickerView: ParentObject) {
            self.pickerView = pickerView
            super.init(parentObject: pickerView, delegateProxy: RxPickerViewDelegateProxy.self)
        }

        // Register known implementationss
        static func registerKnownImplementations() {
            self.register { RxPickerViewDelegateProxy(pickerView: $0) }
        }
    }
#endif
