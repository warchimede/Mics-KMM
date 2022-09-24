# MICS KMM

**Unofficial** SDK interfacing with Mediarithmics API, developed using [Kotlin Multiplatform Mobile](https://kotlinlang.org/docs/multiplatform-mobile-getting-started.html).

[Official documentation](https://developer.mediarithmics.io)

## Version

`1.0.0`

## iOS / tvOS

### Compilation

Execute the `releaseXCFramework` gradle task to build `Mics.xcframework` :

```
./gradlew releaseXCFramework
```

### Installation

Import `Mics.xcframework` in your project.

### Usage

#### Configuration and initialization

```swift
import Mics

// ...

// All of these values are randomly generated for this example
let idfa = "FB408B73-A0CA-49F1-AE8A-8A2E23F520A1"
let idfv = "5E2C708E-067A-4501-BA3D-3BC2E556B268"
let customUserId = "39A608FE-0178-4291-AB63-FEB9D3C82DA0"
let factory = MicsFactory(appId: "3333", apiToken: "g0oKkujyV3G4BaSHkteX7F+dorkFsqAOaE7rbEx/k+JE5fa8LpUDPmkaMq8tzz", datamart: 1000)
let ids = Identifiers(
    vendorId: AccountIdentifier(compartmentId: "2000", identifier: idfv),
    customUserId: AccountIdentifier(compartmentId: "3000", identifier: customUserId),
    advertisingId: idfa)
let mics = factory.create(identifiers: ids)

// ...
```

>🚨 Only pass [advertisingId](https://developer.apple.com/documentation/adsupport/asidentifiermanager/1614151-advertisingidentifier) as `idfa` when authorized to do so and when its value is not all zeros (`00000000-0000-0000-0000-000000000000`)

#### User identifiers association

```swift
mics.associateUserIdentifiers { response, error in
    guard let response else {
        return print(error, error?.localizedDescription)
    }

    print(response.status)
}
```

#### Send tracking event

```swift
let properties: [String: PropertyValue] = [
    "var": PropertyValueString(string: "test"),
    "array": PropertyValueStringList(list: ["elt1", "elt2"]),
    "double": PropertyValueDouble(double: 18.0),
    "float": PropertyValueFloat(float: 8.5),
    "uint": PropertyValueLong(long: 4786876874978)
]

let event = Event(name: .page, properties: properties, timestamp: UInt64(Date().timeIntervalSince1970))
mics.post(event: event) { response, error in
    guard let response else {
        return print(error, error?.localizedDescription)
    }

    print(response.status)
}
```

#### Retrieve segments for the identified user

```swift
mics.getSegments { segments, error in 
    guard let segments else {
        return print(error, error?.localizedDescription)
    }

    print(segments)
}
```

## Author

[William Archimede](http://twitter.com/warchimede)

## License

This project is available under the MIT License.

If you use it and like it, let me know: [@warchimede](http://twitter.com/warchimede)