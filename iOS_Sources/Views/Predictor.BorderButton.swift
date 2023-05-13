//
//  BorderButton.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 29.01.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

extension Predictor {
  final class BorderButton: Button {
    public enum Style {
      case normal, imaged
    }
    
    public var borderWidth: CGFloat = 2.ui {
      didSet {
        layer.borderWidth = borderWidth
      }
    }
    public var borderColor: UIColor = ColorsExportedPublic.provider.predictorPrimaryColor.color {
      didSet {
        layer.borderColor = borderColor.cgColor
      }
    }
    public var title: String = "" {
      didSet {
        setAttributedTitle(title.styled(as: TextStylesExportedPublic.provider.predictorModifyButton),
                           for: .normal)
      }
    }
    public var cornerRadius: CGFloat = 10.ui {
      didSet {
        layer.cornerRadius = cornerRadius
      }
    }
    public private(set) var isAnimated: Bool = false
    
    private var startTitleEdgeInsets: UIEdgeInsets = .zero
    private var startImageEdgeInsets: UIEdgeInsets = .zero
    
    public var style: Style = .normal {
      didSet {
        animationDisposeBag = nil
        setupAnimation()
      }
    }
    
    private let modifiedTitle = StringExportedPublic.provider.getPredictorButtonModified()
      .styled(with: TextStylesExportedPublic.provider.predictorModifiedButton, isUppercased: true)
    private let modifyTitle = StringExportedPublic.provider.getPredictorButtonModify()
      .styled(with: TextStylesExportedPublic.provider.predictorModifyButton, isUppercased: true)
    
    private var animationDisposeBag: DisposeBag? = DisposeBag()
    
    public override func setup() {
      super.setup()
      
      clipsToBounds = true
      
      startTitleEdgeInsets = titleEdgeInsets
      startImageEdgeInsets = imageEdgeInsets
    }
    
    private func setupAnimation() {
      guard style == .imaged else { return }

      let disposeBag = DisposeBag()
      rx.tap
        .asDriver()
        .drive(with: self) { owner, _ in
          owner.isAnimated = true
          owner.isNeedApplyViewStyle = false
          owner.isUserInteractionEnabled = false
          UIView.animate(withDuration: 0.3) {
            ViewStyle.Predictor.Button.green.apply(owner)
            owner.setAttributedTitle(owner.modifiedTitle, for: .normal)
            owner.setImage(ImageExportedPublic.provider.predictorIcModified.image, for: .normal)
          }
          
          let imageViewWidth = owner.imageView?.width.ui ?? 0
          let titleLabelX = owner.titleLabel?.frame.origin.x ?? 0
          let titleLabelWidth = owner.titleLabel?.frame.width.ui ?? 0
          
          owner.imageEdgeInsets = .init(top: 4.ui, left: titleLabelX + titleLabelWidth, bottom: 4.ui, right: 0)
          owner.titleEdgeInsets = .init(top: 0, left: 0, bottom: 0, right: imageViewWidth)
          
          Timer.scheduledTimer(withTimeInterval: .init(3), repeats: false) { [weak owner] _ in
            guard let owner = owner else { return }

            owner.setImage(nil, for: .normal)
            owner.titleEdgeInsets = owner.startTitleEdgeInsets
            owner.imageEdgeInsets = owner.startImageEdgeInsets
            owner.setAttributedTitle(owner.modifyTitle, for: .normal)
            UIView.animate(withDuration: 0.3) {
              ViewStyle.Predictor.Button.border(borderColor: owner.borderColor).apply(owner)
            } completion: { _ in
              owner.isAnimated = false
              owner.isNeedApplyViewStyle = true
              owner.isUserInteractionEnabled = true
            }
          }
        }.disposed(by: disposeBag)
      animationDisposeBag = disposeBag
    }
  }
}
