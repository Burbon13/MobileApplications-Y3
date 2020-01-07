# Planes Manager - keep track of your fleet!

A project used to manage your personal fleet of planes.

### Android screenshots

<img src="/screenshots/a5.jpeg" width="250"> <img src="/screenshots/a3.jpeg" width="250"> <img src="/screenshots/a1.jpeg" width="250"> <img src="/screenshots/a8.jpeg" width="250"> <img src="/screenshots/a6.jpeg" width="250"> <img src="/screenshots/a7.jpeg" width="250"> <img src="/screenshots/a2.jpeg" width="250">

### React Native screenshots

<img src="/screenshots/rn7.jpeg" width="250"> <img src="/screenshots/rn6.jpeg" width="250"> <img src="/screenshots/rn5.jpeg" width="250"> <img src="/screenshots/rn3.jpeg" width="250"> <img src="/screenshots/rn4.jpeg" width="250"> <img src="/screenshots/rn2.jpeg" width="250"> <img src="/screenshots/rn1.jpeg" width="250">

## Contents

- **PlanesManager\_Server_Node** Server built with Node.js
- **PlanesManager_Android** Client mobile application built with Android Kotlin
- **PlanesManager_ReactNative** Client mobile application built with React Native

### Requirements

 * [Node.js][node]
 * [npm][npm] (normally comes with Node.js)
 * [Rect Native dependencies][react-native-dep]
 * [Android Studio][android-studio] (Android development environment for React Native and native Android)

[node]: https://nodejs.org/
[npm]: https://www.npmjs.com/
[react-native-dep]: https://facebook.github.io/react-native/docs/getting-started
[android-studio]: https://developer.android.com/studio/index.html

## Usage

To start the server enter **PlanesManager\_Server_Node** and run the following commands:

```shell
npm install # Only once (installs the dependencies)
npm start # Runs the server
```

To run the native Android application enter **PlanesManager\_Android** and:

...

To run the React Native application enter **PlanesManager\_ReactNative** and run the following commands:

```shell
npm install # Only once (installs the dependencies)
react-native run-android # To run on an emulator or Android physical device (needs to be connected to the computer)
```

## Reflection

This was a project built during my Mobile Applications module at Babes Bolyai University.

### Outcomes

I have gained a better understanding of the following concepts:

- Android
  - Complex lifecycle of applications, activities and fragments
  - App architecture using ViewModel
  - Navigation component
  - Room (persistence library)
  - Retrofit (REST Client library)
  - Broadcasts
  - Animations
- Coroutines in Kotlin
- JSON Web Tokens
- React Native
  - Component lifecycle, props and state
  - Functional components
  - Hooks
  - Async processing: Promise
  - Async storage
  - Animations
