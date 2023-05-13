//
//  PageControl.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 4/20/20.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

class PageControl: UIView {
  open var numberOfPages: Int = 0 {
    didSet {
      updateNumberOfPages(numberOfPages)
    }
  }
  open var progress: CGFloat = 0 {
    didSet {
      layoutActivePageIndicator(progress)
    }
  }
  open var currentPage: Int {
    Int(round(progress))
  }
  
  open var activeTint: UIColor = UIColor.white {
    didSet {
      activeLayer.backgroundColor = activeTint.cgColor
    }
  }
  open var inactiveTint: UIColor = UIColor(white: 1, alpha: 0.3) {
    didSet {
      inactiveLayers.forEach { $0.backgroundColor = inactiveTint.cgColor }
    }
  }
  open var indicatorPadding: CGFloat = 10 {
    didSet {
      layoutInactivePageIndicators(inactiveLayers)
      layoutActivePageIndicator(progress)
      self.invalidateIntrinsicContentSize()
    }
  }
  open var indicatorRadius: CGFloat = 5 {
    didSet {
      layoutInactivePageIndicators(inactiveLayers)
      layoutActivePageIndicator(progress)
      self.invalidateIntrinsicContentSize()
    }
  }
  
  fileprivate var indicatorDiameter: CGFloat {
    indicatorRadius * 2
  }
  fileprivate var inactiveLayers = [CALayer]()
  fileprivate lazy var activeLayer: CALayer = { [unowned self] in
    let layer = CALayer()
    layer.frame = CGRect(origin: CGPoint.zero,
                         size: CGSize(width: self.indicatorDiameter, height: self.indicatorDiameter))
    layer.backgroundColor = self.activeTint.cgColor
    layer.cornerRadius = self.indicatorRadius
    layer.actions = [
      "bounds": NSNull(),
      "frame": NSNull(),
      "position": NSNull()]
    return layer
  }()
  
  fileprivate func updateNumberOfPages(_ count: Int) {
    guard count != inactiveLayers.count else { return }
    inactiveLayers.forEach { $0.removeFromSuperlayer() }
    inactiveLayers = [CALayer]()
    inactiveLayers = stride(from: 0, to: count, by: 1).map { _ in
      let layer = CALayer()
      layer.backgroundColor = self.inactiveTint.cgColor
      self.layer.addSublayer(layer)
      return layer
    }
    layoutInactivePageIndicators(inactiveLayers)
    self.layer.addSublayer(activeLayer)
    layoutActivePageIndicator(progress)
    self.invalidateIntrinsicContentSize()
  }
  
  fileprivate func layoutActivePageIndicator(_ progress: CGFloat) {
    guard progress >= 0 && progress <= CGFloat(numberOfPages - 1) else { return }
    let denormalizedProgress = progress * (indicatorDiameter + indicatorPadding)
    let distanceFromPage = abs(round(progress) - progress)
    let width = indicatorDiameter
    + indicatorPadding * (distanceFromPage * 2)
    var newFrame = CGRect(x: 0,
                          y: activeLayer.frame.origin.y,
                          width: width,
                          height: indicatorDiameter)
    newFrame.origin.x = denormalizedProgress
    activeLayer.cornerRadius = indicatorRadius
    activeLayer.frame = newFrame
  }
  
  fileprivate func layoutInactivePageIndicators(_ layers: [CALayer]) {
    var layerFrame = CGRect(x: 0, y: 0, width: indicatorDiameter, height: indicatorDiameter)
    layers.forEach { layer in
      layer.cornerRadius = indicatorRadius
      layer.frame = layerFrame
      layerFrame.origin.x += indicatorDiameter + indicatorPadding
    }
  }
  
  override open var intrinsicContentSize: CGSize {
    sizeThatFits(CGSize.zero)
  }
  
  override open func sizeThatFits(_ size: CGSize) -> CGSize {
    .init(width: CGFloat(inactiveLayers.count) * indicatorDiameter + CGFloat(inactiveLayers.count - 1) * indicatorPadding,
          height: indicatorDiameter)
  }
}
