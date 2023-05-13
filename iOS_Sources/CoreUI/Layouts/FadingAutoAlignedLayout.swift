//
//  FadingLayout.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 2.03.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit


class FadingAutoAlignedLayout: AutoAlignedCollectionViewLayout {
  enum Position {
    case top
    case bottom
    case center
  }
  
  private let fadeFactor: CGFloat
  private let position: Position
  
  required init?(coder aDecoder: NSCoder) {
    self.fadeFactor = 0.5
    self.position = .center
    super.init(coder: aDecoder)
  }
  
  public init(fadeFactor: CGFloat = 0.5, position: Position = .center, settings: AutoAlignedCollectionViewLayout.Settings) {
    assert((CGFloat(0)...CGFloat(1)) ~= fadeFactor, "Fade factor must be between 0 and 1")
    self.fadeFactor = fadeFactor
    self.position = position
    super.init(settings: settings)
  }
  
  public override func prepare() {
    super.prepare()
  }
  
  public override func layoutAttributesForElements(in rect: CGRect) -> [UICollectionViewLayoutAttributes]? {
    guard  let collectionView = self.collectionView, let attributes = super.layoutAttributesForElements(in: rect) else { return nil }
    
    let visibleRect = CGRect(origin: collectionView.contentOffset, size: collectionView.bounds.size)
    
    attributes.forEach {
      if $0.frame.intersects(rect) {
        var distance = scrollDirection == .horizontal ? visibleRect.midX - $0.center.x : visibleRect.midY - $0.center.y
        switch position {
        case .top:
          distance = scrollDirection == .horizontal ? visibleRect.minX - $0.center.x : visibleRect.minY - $0.center.y
        case .bottom:
          distance = scrollDirection == .horizontal ? visibleRect.maxX - $0.center.x : visibleRect.maxY - $0.center.y
        default: break
        }
        let normalizedDistance = abs(distance) / (visibleRect.height * fadeFactor)
        let fade = 1 - normalizedDistance
        $0.alpha = fade
      }
    }
    
    return attributes
  }
  
  public override func initialLayoutAttributesForAppearingItem(at itemIndexPath: IndexPath) -> UICollectionViewLayoutAttributes? {
    guard let attributes = super.layoutAttributesForItem(at: itemIndexPath) else { return nil }
    attributes.alpha = 0
    
    return attributes
  }
  
  public override func finalLayoutAttributesForDisappearingItem(at itemIndexPath: IndexPath) -> UICollectionViewLayoutAttributes? {
    guard let attributes = super.layoutAttributesForItem(at: itemIndexPath) else { return nil }
    attributes.alpha = 0
    
    return attributes
  }
}
