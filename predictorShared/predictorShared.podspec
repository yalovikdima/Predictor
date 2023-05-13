Pod::Spec.new do |s|
  s.name = "predictorShared"
  s.version = "0.0.1"
  s.summary = "Predictor Shared"

  s.homepage = "https://github.com/netcosports"
  s.license = {
    :type => "Copyright",
    :text => "Copyright 2021 Origins Digital"
  }
  s.author = {
   "Eugene Filipkov" => "eugene_f@origins-digital.com"
  }

  s.source = { :git => "git@github.com:netcosports/Predictor.git" }
  s.platform = :ios
  s.ios.deployment_target = "11.0"

  s.static_framework = true

  s.vendored_frameworks = "build/predictorShared.xcframework"
end
