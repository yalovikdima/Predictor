//
//  Predictor.Share.QRView.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 22.03.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//
//

import UIKit
import CoreImage

extension Predictor.Share {
  final class QRView: CoreUI.View, ViewReusable {
    public struct ViewModel {
      public let absoluteString: String?
      
      public init(url: URL?) {
        absoluteString = url?.absoluteString
      }
    }
    
    private let qrImageView = ImageView()
    private let borderTopImageView = ImageView {
      $0.image = ImageExportedPublic.provider.predictorQrCodeStroke.image
    }
    private let borderBottomImageView = ImageView {
      $0.image = ImageExportedPublic.provider.predictorQrCodeStroke.image
    }
    
    public override func setup() {
      super.setup()
      
      addSubviews(borderTopImageView, borderBottomImageView, qrImageView)
      borderBottomImageView.transform = borderBottomImageView.transform.rotated(by: .pi)
    }
    
    public override func layoutSubviews() {
      super.layoutSubviews()
      
      let size = CGSize(width: 11.ui, height: 10.ui)
      borderTopImageView.pin.start().top().size(size)
      borderBottomImageView.pin.end().bottom().size(size)
      qrImageView.pin.all(5.ui)
    }
    
    public typealias Data = ViewModel
    public func setup(with data: Data) {
      qrImageView.image = generateQRCode(from: data.absoluteString)
      setNeedsLayout()
    }
    
    private func generateQRCode(from string: String?) -> UIImage? {
      guard let string = string else {  return nil }
      
      let data = string.data(using: String.Encoding.ascii)
      
      if let filter = CIFilter(name: "CIQRCodeGenerator") {
        filter.setValue(data, forKey: "inputMessage")
        let transform = CGAffineTransform(scaleX: 3, y: 3)
        
        if let output = filter.outputImage?.transformed(by: transform) {
          return UIImage(ciImage: output)
        }
      }
      
      return nil
    }
  }
}
