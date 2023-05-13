//
//  Predictor.SponsorView.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 2.02.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

extension Predictor.Sponsor {
  final class View: CoreUI.View, ViewReusable {
    private let titleLabel = CLabel {
      $0.numberOfLines = 0
    }
    private let imageView = ImageView {
      $0.contentMode = .scaleAspectFit
    }
    
    fileprivate let open = PublishSubject<URL?>()
    
    private var setupDisposeBag = DisposeBag()
    
    public override func setup() {
      super.setup()
      addSubviews(titleLabel, imageView)
    }
    
    public override func layoutSubviews() {
      super.layoutSubviews()
      
      performLayout()
    }
    
    public override func sizeThatFits(_ size: CGSize) -> CGSize {
      autoSizeThatFits(size, layoutClosure: performLayout)
    }
    
    private func performLayout() {
      titleLabel.pin.top().horizontally().width(85.ui).sizeToFit(.width)
      imageView.pin.below(of: titleLabel, aligned: .center).marginTop(3.ui).bottom().size(60.ui)
    }
    
    public typealias Data = SponsorUiKMM
    public func setup(with model: Data) {
      titleLabel.attributedText = model.presentedBy.attributedString
      _ = imageView.setupImage(with: model.imageUrl.url)
      setNeedsLayout()
      
      let disposeBag = DisposeBag()
      rx.tapGesture().when(.recognized).map { _ in model.url.url }.bind(to: open).disposed(by: disposeBag)
      setupDisposeBag = disposeBag
    }
  }
}

extension Reactive where Base: Predictor.Sponsor.View {
  var open: ControlEvent<URL?> {
    ControlEvent(events: base.open)
  }
}
