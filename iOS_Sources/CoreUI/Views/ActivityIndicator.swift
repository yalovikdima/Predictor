//
//  ActivityIndicator.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 3/17/20.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

public protocol ActivityIndicatorOverriding where Self: UIView {
  var isAnimating: Bool { get }
  
  func startAnimating()
  func stopAnimating()
}

protocol ActivityIndicatable {
  var hidesWhenStopped: Bool { get set }
  var color: UIColor? { get set }
  var isAnimating: Bool { get }
  
  func startAnimating()
  func stopAnimating()
}

class ActivityIndicator: CoreUI.View {
  private var activityIndicator: NVActivityIndicatorView?
  
  public var hidesWhenStopped: Bool = true { didSet {  } }
  
  open override var frame: CGRect {
    get { return super.frame }
    set { super.frame = normalized(rect: newValue) }
  }
  
  open override var bounds: CGRect {
    get { return super.bounds }
    set { super.bounds = normalized(rect: newValue) }
  }
  
  // MARK: - Lifecycle
  
  open override func setup() {
    super.setup()
    activityIndicator = NVActivityIndicatorView(frame: CGRect(origin: .zero, size: CGSize(side: 40.0)),
                                                type: .circleStrokeSpin,
                                                color: color)
    guard let activityIndicator = activityIndicator else { return }
    
    addSubview(activityIndicator)
    isUserInteractionEnabled = false
  }
  
  open override func layoutSubviews() {
    super.layoutSubviews()
    guard let activityIndicator = activityIndicator else { return }
    
    activityIndicator.pin.pinEdges()
  }
}

// MARK: - ActivityIndicatable

extension ActivityIndicator: ActivityIndicatable {
  public var isAnimating: Bool {
    return activityIndicator?.isAnimating ?? false
  }
  
  public func startAnimating() {
    activityIndicator?.startAnimating()
  }
  
  public func stopAnimating() {
    activityIndicator?.stopAnimating()
  }
  
  public var color: UIColor? {
    get { return activityIndicator?.color }
    set { activityIndicator?.color = newValue ?? .black }
  }
}

// MARK: - Private

private extension ActivityIndicator {
  func normalized(rect: CGRect) -> CGRect {
    let mid = CGPoint(x: rect.midX, y: rect.midY)
    let side: CGFloat = 40.0
    let result = CGRect(x: mid.x - side * 0.5, y: mid.y - side * 0.5, width: side, height: side)
    return result
  }
}
