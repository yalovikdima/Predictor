//
//  Predictor.Gift.View.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 17.05.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

extension Predictor.Gift {
  final class View: CoreUI.View, ViewReusable {
    private let borderView = CoreUI.View {
      $0.viewStyle = ViewStyle.Predictor.View.gift(borderColor: ColorsExportedPublic.provider.predictorGiftBkgBorder.color)
    }
    private let titleLabel = CLabel {
      $0.backgroundColor = .clear
      $0.numberOfLines = 2
    }
    private let imageView = ImageView {
      $0.contentMode = .scaleAspectFit
    }
    
    fileprivate let open = PublishSubject<Void>()
    
    private var setupDisposeBag = DisposeBag()
    
    public override func setup() {
      super.setup()
      
      addSubviews(borderView, titleLabel, imageView)
    }
    
    public override func layoutSubviews() {
      super.layoutSubviews()
      
      borderView.pin.horizontally().vCenter().height(41.ui)
      imageView.pin.right(to: borderView.edge.right).marginRight(13.ui).vertically().size(.init(82.ui))
      titleLabel.pin.start(21.ui).vCenter(to: borderView.edge.vCenter).height(of: borderView).end(to: imageView.edge.left)
      
      borderView.shadow = .sketch(color: UIColor.black.withAlphaComponent(0.16), x: 0, y: 0, blur: 6.ui, spread: 0)
    }
    
    public typealias Data = GiftUiKMM
    public func setup(with data: Data) {
      titleLabel.attributedText = data.title?.attributedString
      _ = imageView.setupImage(with: data.imageUrl.url)
      setNeedsLayout()
      let disposeBag = DisposeBag()
      rx.tapGesture().when(.recognized).map { _ in () }.bind(to: open).disposed(by: disposeBag)
      setupDisposeBag = disposeBag
    }
  }
}

extension Reactive where Base: Predictor.Gift.View {
  var open: ControlEvent<Void> {
    ControlEvent(events: base.open)
  }
}
