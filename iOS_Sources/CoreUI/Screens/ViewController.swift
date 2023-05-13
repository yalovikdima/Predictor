//
//  ViewController.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 3/17/20.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

extension CoreUI {
  open class ViewController: UIViewController {
    public let disposeBag = DisposeBag()
    open var backButtonColor: UIColor { .black }
    
    private let titleLabel = CLabel {
      $0.numberOfLines = 1
      $0.textAlignment = .center
    }
    
    private let backButton = UIBarButtonItem(image: nil,
                                             style: .plain,
                                             target: nil,
                                             action: nil)
    
    public init() {
      super.init(nibName: nil, bundle: nil)
    }
    
    public required init?(coder aDecoder: NSCoder) {
      fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Overrides
    open override var prefersStatusBarHidden: Bool { false }
    open override var shouldAutorotate: Bool { true }
    open override var supportedInterfaceOrientations: UIInterfaceOrientationMask { .portrait }
    
    open override var hidesBottomBarWhenPushed: Bool {
      get { return false }
      set { super.hidesBottomBarWhenPushed = newValue }
    }
    
    open override var prefersHomeIndicatorAutoHidden: Bool {
      return true
    }
    
    open override var preferredStatusBarStyle: UIStatusBarStyle {
      return .lightContent
    }
    
    // MARK: - Lifecycle
    open override func loadView() {
      super.loadView()
    }
    
    open override func viewDidLoad() {
      super.viewDidLoad()
      
      navigationItem.leftItemsSupplementBackButton = true
      
      backButton.rx.tap.observe(on: MainScheduler.asyncInstance).subscribe(onNext: { [weak self] _ in
        self?.navigationController?.popViewController(animated: true)
      }).disposed(by: disposeBag)
    }
    
    open override func viewWillAppear(_ animated: Bool) {
      super.viewWillAppear(animated)
      setupNavigationItem()
    }
    
    open func setupNavigationItem() {
      navigationItem.leftBarButtonItem = leftBarButtonItem
      navigationController?.navigationBar.tintColor = backButtonColor
      navigationController?.navigationBar.subviews.forEach { $0.clipsToBounds = false }
    }
    
    open func setupNavigationItemTitle(_ title: NSAttributedString? = nil) {
      guard let title = title else { return }
      navigationItem.titleView = titleView(title: title)
    }
    
    open var leftBarButtonItem: UIBarButtonItem? {
      if let vcCount = navigationController?.viewControllers.count, vcCount > 1 {
        return backButton
      } else {
        return nil
      }
    }
    
    open func updateBackButton(with color: UIColor?) {
      navigationItem.leftBarButtonItem?.tintColor = color
    }
    
    open func clearBackButton() {
      navigationItem.leftBarButtonItem = nil
    }
    
    private func titleView(title: NSAttributedString) -> UIView? {
      let frame = CGRect(size: .init(width: 250, height: 44))
      let view = View(frame: frame)
      view.clipsToBounds = false
      view.addSubview(titleLabel)
      titleLabel.attributedText = title
      titleLabel.pin.all()
      
      return view
    }
  }
}
