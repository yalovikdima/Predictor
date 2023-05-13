//
//  ImageView.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 3/31/20.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

class ImageView: UIImageView {
  open override class var requiresConstraintBasedLayout: Bool { false }
  
  // MARK: - Init
  
  public convenience init() {
    self.init(frame: .zero)
  }
  
  public override convenience init(image: UIImage?) {
    let frame = image.flatMap { CGRect(origin: .zero, size: $0.size) } ?? .zero
    self.init(frame: frame)
    self.image = image
  }
  
  public override init(frame: CGRect) {
    super.init(frame: frame)
  }
  
  public required init?(coder aDecoder: NSCoder) {
    super.init(coder: aDecoder)
  }
  
  // MARK: - Public
  public func setupImageRx(with url: URL?,
                           processors: [ImageProcessing] = [],
                           placeholder: UIImage? = nil,
                           thumbnailMaxPixelSize: CGFloat = 0) -> Single<ImageResponse> {
    guard let url = url else {
      return .create { [weak self] single -> Disposable in
        self?.image = placeholder
        single(.failure(FetchImageError.sourceEmpty))
        return Disposables.create()
      }
    }
    let thumbnail = ImageRequest.ThumbnailOptions(maxPixelSize: thumbnailMaxPixelSize * Screen.scale)
    let request = ImageRequest(url: url,
                               processors: processors,
                               priority: .high,
                               userInfo: thumbnailMaxPixelSize > 0 ? [.thumbnailKey: thumbnail] : [:])
    let options = ImageLoadingOptions(placeholder: placeholder,
                                      transition: .fadeIn(duration: 0.3),
                                      failureImage: placeholder)
    
    return .create { [weak self] single -> Disposable in
      guard let self = self else { return Disposables.create() }
      
      let task = loadImage(with: request, options: options, into: self) { result in
        switch result {
        case let .success(response):
          single(.success(response))
        case let .failure(error):
          single(.failure(error))
        }
      }
      
      return Disposables.create { task?.cancel() }
    }
  }
  
  public func setupImage(with url: URL?,
                         processors: [ImageProcessing] = [],
                         placeholder: UIImage? = nil,
                         thumbnailMaxPixelSize: CGFloat = 0,
                         completion: ((Result<ImageResponse, ImagePipeline.Error>) -> Void)? = nil) -> ImageTask? {
    let thumbnail = ImageRequest.ThumbnailOptions(maxPixelSize: thumbnailMaxPixelSize * Screen.scale)
    let request = ImageRequest(url: url,
                               processors: processors,
                               priority: .high,
                               userInfo: thumbnailMaxPixelSize > 0 ? [.thumbnailKey: thumbnail] : [:])
    let options = ImageLoadingOptions(placeholder: placeholder,
                                      transition: .fadeIn(duration: 0.3),
                                      failureImage: placeholder)
    
    return loadImage(with: request, options: options, into: self, completion: completion)
  }
}
