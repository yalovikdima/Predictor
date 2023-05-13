# Predictor

Predictor based on KMM

# iOS

## Setup project

1. Run command :

```
bundle exec fastlane customize customization:<customization_name> env:<environment_name>
```

Available customization see in fastlane/customizations path.

2. Run command:

```
bundle exec fastlane prepare env::<environment_name>
```

3. Launch iosApp.xcworkspace.

## Integration

### Pods

You have two links e.g.:

```
https://origins-mobile-products.s3.eu-west-1.amazonaws.com/.../1.0.67/predictorShared.podspec
https://origins-mobile-products.s3.eu-west-1.amazonaws.com/.../1.0.67/PredictorSDK.podspec
```

Add it to your Podfile like this:

```
pod 'predictorShared', :podspec => 'https://origins-mobile-products.s3.eu-west-1.amazonaws.com/.../1.0.67/predictorShared.podspec'
pod 'PredictorSDK', :podspec => 'https://origins-mobile-products.s3.eu-west-1.amazonaws.com/.../1.0.67/PredictorSDK.podspec'
```

If you have some errors after call

```
pod install
```

Create local Podspecs motmShared.podspec and MotmSDK.podspec. Copy content from links and add to Podfile like this:

```
pod 'predictorShared', :podspec => '<Your_project_path>/predictorShared.podspec'
pod 'PredictorSDK', :podspec => '<Your_project_path>/PredictorSDK.podspec'
```

### SPM

You have link to Package.swift file e.g.

```
https://origins-mobile-products.s3.eu-west-1.amazonaws.com/.../1.0.67/Package.swift
```

Download Package.swift file and open to your packages(like file://<path_to_file>/Package.swift).

### Added PredictorController

Initialize PredictorSDK

```swift
PredictorSDK.initialization(settings: PredictorSettings)
```

### Parameters

```settings``` - This structure like:

```swift
public struct PredictorSettings {
  public enum LogLevel {
    case none, all
  }
  public enum Environment {
    case development(key: String), staging(key: String), production(key: String)
  }
  
  public struct Share {
    public let appURL: URL?
  }

  public let environment: Environment
  public let share: Share
  public let analytics: ((AnalyticsActions) -> Void)?
  public let logLevel: LogLevel
}
```

```environment``` - This your current environment and secret key(it's unique for customers).

```appURL``` - URL when encoded in QR code for sharing.

```analytics``` - Closure when return analytics events.

``` logLevel``` - In current moment have only two values all and none. None - turn off all loging.

Create PredictorController
```swift
PredictorSDK.controller(with id: String, rootController: UIViewController, overriding: Overriding? = nil)
```

### Parameters

```id``` - This your current match id.

```rootController``` - Place this your root controller.(This MUST be parent controller of your controller, which have size of device screen)

```overriding``` - Protocol like:

```swift
public protocol Overriding: AnyObject {
	var activityIndicator: ActivityIndicatorOverriding? { get }
}
```

```activityIndicator``` - Use if you need override standart activity indicator. For this
realize ```ActivityIndicatorOverriding``` protocol.

```swift
public protocol ActivityIndicatorOverriding where Self: UIView {
	var isAnimating: Bool { get }

	func startAnimating()
	func stopAnimating()
}
```

If you have login in your app. Use ```PredictorSDK.profileService()``` it realize ```ProfileService``` protocol:
```swift
public protocol ProfileService {
	func register(profileRequiredClosure: @escaping () -> ())
	func set(state: ProfileState)
}
```

```register(profileRequiredClosure: @escaping () -> ())``` - Place your show login logic to this closure.
```set(state: ProfileState)``` - Use this function for notify framework of hange login state.

```swift
public enum ProfileState {
	case loggedIn(model: LoginModel), loggedOut
}
``` 

Checking game availability
```swift
PredictorSDK.isGameAvailable(id: String, completition: @escaping (Bool, Error?) -> Void)
```

### Parameters

```id``` - This your current match id.

```completition``` - Result closure.

### Info.plist UsageDescription

Adding NSPhotoLibraryAddUsageDescription for image sharing.

# Android

## Download

```
implementation("com.netcosports.andgaming:predictor-CUSTOMIZATION:$version")
```

## Usage

1. Initialize

```kotlin
Predictor.init(
    accountKey = "accountKey",
    api = Predictor.Api.DEV,
    isUseNativeAppLogin = true,
    isLogsEnabled = true,
    context = context,
    analytics = analytics, //not mandatory
    qrCode = "https://www.origins-digital.com/"
)
```

### Parameters

```accountKey``` - Secret key

```api``` - This your current environment.

```kotlin
   object Predictor {

    enum class Api {
        DEV, STAGING, PROD
    }
}
```

```isUseNativeAppLogin``` - ```true``` if your app has native authorization. You should
subscribe ```Predictor.showLogin: MutableLiveData<Boolean>``` and run authorization page if ```value``` is ```true```.
Then call ```Predictor.loginConsumed()``` after authorization. If ```isUseNativeAppLogin = false``` predictor will be used
native login page.

```isLogsEnabled``` - switch on/off logging.

```analytics``` - object implementation interface ```PredictorAnalytics```

```qrCode``` - Sharing link. ```null``` - qrcode is not shown in the sharing image.

```kotlin
interface PredictorAnalytics {

    fun onPredictorOpened(game: GameEntity)
    fun onValidated(score: ScoreUI, game: GameEntity)
    fun onModified(score: ScoreUI, game: GameEntity)
}
```

2. User data

```kotlin
  Predictor.setUserData(
    externalUserId = "externalUserId",
    firstName = "firstName",
    lastName = "lastName"
)
```

3. Get fragment

```kotlin
  Predictor.getFragment(
    matchId = matchId // match id from match center
)
```

## checking available game
```kotlin
    Predictor.checkExistGame(matchId = matchId) // true or false
```
