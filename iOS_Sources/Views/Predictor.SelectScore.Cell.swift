//
//  Predictor.NumberCell.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 3.02.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

extension Predictor.SelectScore {
  final class Cell: CoreUI.Cell, Reusable, Eventable {
    public enum Actions: Hashable {
      case none, tap(index: Int)
    }
    
    public enum Style {
      case normal, disabled, share
    }
    
    public struct ViewModel: Hashable {
      public let number: NSAttributedString
      public let numberFocused: NSAttributedString
      
      public init(number: String, style: Style = .normal) {
        let numberStyle: Attributable
        let numberFocusedStyle: Attributable
        switch style {
        case .disabled:
          numberStyle = TextStylesExportedPublic.provider.predictorNumberPickerDisableUnselected
          numberFocusedStyle = TextStylesExportedPublic.provider.predictorNumberPickerDisableSelected
        case .share:
          numberStyle = TextStylesExportedPublic.provider.predictorNumberPickerShareUnselected
          numberFocusedStyle = TextStylesExportedPublic.provider.predictorNumberPickerShareSelected
        default:
          numberStyle = TextStylesExportedPublic.provider.predictorNumberPickerUnselected
          numberFocusedStyle = TextStylesExportedPublic.provider.predictorNumberPickerSelected
        }
        self.number = number.styled(as: numberStyle)
        self.numberFocused = number.styled(as: numberFocusedStyle)
      }
    }
    
    public typealias Event = Actions
    public typealias Data = ViewModel
    
    public var data: ViewModel?
    public var eventSubject = PublishSubject<Event>()
    
    private let numberLabel = CLabel()
    private let numberFocusedLabel = CLabel()
    
    public override func setup() {
      super.setup()
      
      contentView.addSubviews(numberLabel, numberFocusedLabel)
    }
    
    public override func layoutSubviews() {
      super.layoutSubviews()
      
      numberLabel.pin.sizeToFit().center()
      numberFocusedLabel.pin.sizeToFit().center()
    }
    
    public func setup(with data: Data) {
      numberLabel.attributedText = data.number
      numberFocusedLabel.attributedText = data.numberFocused
      setNeedsLayout()
    }
    
    public static func size(for data: Data, containerSize: CGSize) -> CGSize { .init(width: containerSize.width, height: 40.ui) }
    
    public override func apply(_ layoutAttributes: UICollectionViewLayoutAttributes) {
      super.apply(layoutAttributes)
      
      guard let attributes = layoutAttributes as? AutoAlignedCollectionViewLayoutAttributes else { return }
      
      numberLabel.alpha = 1.0 - attributes.progress
      numberFocusedLabel.alpha = attributes.progress
      setNeedsLayout()
    }
  }
}
