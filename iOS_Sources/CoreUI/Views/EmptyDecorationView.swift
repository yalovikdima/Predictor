//
//  EmptyDecorationView.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 3/31/20.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import Foundation
import UIKit

extension Observable: Equatable, Hashable where Element == NSAttributedString {
  static func == (lhs: Observable<Element>, rhs: Observable<Element>) -> Bool {
    return lhs === rhs
  }

  func hash(into hasher: inout Hasher) {
    hasher.combine(self)
  }
}

class EmptyDecorationView: DecorationCollectionViewCell<Observable<NSAttributedString>> {
  open override class var zIndex: Int {
    return 50
  }

  let label = CLabel {
    $0.numberOfLines = 0
  }

  public override func setup() {
    super.setup()
    contentView.backgroundColor = .clear
    contentView.addSubview(label)
  }

  public override func layoutSubviews() {
    super.layoutSubviews()
    label.pin.all()
  }

  var reuseDisposeBag: DisposeBag?
  open override func setup(with data: Data) {
    let reuseDisposeBag = DisposeBag()
    data.bind(onNext: { [weak self] text in
      self?.label.attributedText = text
      self?.label.textAlignment = .center
    }).disposed(by: reuseDisposeBag)
    self.reuseDisposeBag = reuseDisposeBag
  }
}
