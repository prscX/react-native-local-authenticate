<h1 align="center">

  <p align="center">
    <a href="https://www.npmjs.com/package/react-native-local-authenticate"><img src="http://img.shields.io/npm/v/react-native-local-authenticate.svg?style=flat" /></a>
    <a href="https://github.com/prscX/react-native-local-authenticate/pulls"><img alt="PRs Welcome" src="https://img.shields.io/badge/PRs-welcome-brightgreen.svg" /></a>
    <a href="https://github.com/prscX/react-native-local-authenticate#License"><img src="https://img.shields.io/npm/l/react-native-local-authenticate.svg?style=flat" /></a>
  </p>

    ReactNative: Local Authenticate [FaceID/TouchID] (Android/iOS)

If this project has helped you out, please support us with a star 🌟

</h1>

This library is the replica of expo-local-authentication source. It can be used as standalone library in case someone does not wants to configure expo environment.

## 📖 Getting started

`$ npm install react-native-local-authenticate --save`

`$ react-native link react-native-local-authenticate`

- **Android**

  - Please add below script in your `build.gradle`

```
buildscript {
    repositories {
        jcenter()
        maven { url "https://maven.google.com" }
        ...
    }
}

allprojects {
    repositories {
        mavenLocal()
        jcenter()
        maven { url "https://maven.google.com" }
        ...
    }
}
```

> **Note**
>
> - Android SDK 27 > is supported

## 💬 Usage

```
import { RNLocalAuthenticate } from 'react-native-local-authenticate'

RNLocalAuthenticate.HasHardware().then((hasHardware) => {
})

RNLocalAuthenticate.IsEnrolled().then((isEnrolled) => {
})

RNLocalAuthenticate.SupportedAuthenticationTypes().then((isEnrolled) => {
})

RNLocalAuthenticate.Authenticate('reason').then((isEnrolled) => {
})

```


## 💡 Props

- **General(iOS & Android)**

| Prop                   | Type                | Default | Note                                             |
| ---------------------- | ------------------- | ------- | ------------------------------------------------ |
| `HasHardware:Promise`     | `bool`            |         | Used to check for hardware capability                 |
| `IsEnrolled:Promise`                | `bool`            |         | Used to check enrolled for FaceID/TouchID                        |
| `Authenticate`          | `bool`            |         | Used to authenticate user using FaceId/TouchID                  |

- **Android**

| Prop                     | Type                | Default | Note                                                                                                                                                                           |
| ------------------------ | ------------------- | ------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `CancelAuthenticate`      | ``              | true    | Used to cancel ongoing authentication                                                                    |

- **iOS**

| Prop                         | Type                | Default      | Note                                                       |
| ---------------------------- | ------------------- | ------------ | ---------------------------------------------------------- |
| `SupportedAuthenticationTypes`      | `array` |  | Used to check supported authentication types                            |

## ✨ Credits

- [Expo: Local Authentication](https://docs.expo.io/versions/latest/sdk/local-authentication/)

## 🤔 How to contribute

Have an idea? Found a bug? Please raise to [ISSUES](https://github.com/prscX/react-native-local-authenticate/issues).
Contributions are welcome and are greatly appreciated! Every little bit helps, and credit will always be given.

## 💫 Where is this library used?

If you are using this library in one of your projects, add it in this list below. ✨

## 📜 License

This library is provided under the Apache 2 License.

RNAppTour @ [prscX](https://github.com/prscX)

## 💖 Support my projects

I open-source almost everything I can, and I try to reply everyone needing help using these projects. Obviously, this takes time. You can integrate and use these projects in your applications for free! You can even change the source code and redistribute (even resell it).

However, if you get some profit from this or just want to encourage me to continue creating stuff, there are few ways you can do it:

- Starring and sharing the projects you like 🚀
- If you're feeling especially charitable, please follow [prscX](https://github.com/prscX) on GitHub.

  <a href="https://www.buymeacoffee.com/prscX" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png" alt="Buy Me A Coffee" style="height: auto !important;width: auto !important;" ></a>

  Thanks! ❤️
  <br/>
  [prscX.github.io](https://prscx.github.io)
  <br/>
  </ Pranav >
