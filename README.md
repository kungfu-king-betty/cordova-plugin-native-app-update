cordova-plugin-native-app-update &middot; [![npm](https://img.shields.io/npm/dm/cordova-plugin-native-app-update.svg)]() [![npm](https://img.shields.io/npm/v/cordova-plugin-native-app-update.svg)]()
==============================================================================

By [**Austen Zeh**](https://www.linkedin.com/in/austen-zeh-20bb55128/)

------------------------------------------------------------------------------

App Update is simple plugin for iOS and Android which will look for an app update through the App Store and Google Play Store respectively. This plugin was *built to assist with the inconsistent review times by both Apple and Google* after rolling out a new release version to production. This may also allow you to rollout a new version release for one OS, but not the other.

This plugin is defined as native because the app information is pulled right from the applicatoion itself using the native OS. Since the app information is gathered by the plugin, there is **no need to provide an appID**. However, because the plugin must make a request to the either of the stores, the app **will need a connection** to make the request, and the update process will have to wait for the success callback to be executed. For more information of this please look below at the examples.

:new: App Update now provides a [__`needsUpdate`__](#example) method to check if an update is available in the OS store.

### iOS

- Currently for iOS devices, the plugin **checks the version number, not the build number**, so if you release multiple builds within the same version, then this plugin will not handle these updates properly. (ex. 1.0.0 != 1.0.1)
- This plugin may be updated in the future to handle multiple builds within the same version, but seeing as Apple requires a new version number for new releases this may not be needed at all.
- [__`needsUpdate`__](#example) will return either true or false when successful.

### Android

- The plugin uses Android's [**in-app updates functionality**](https://developer.android.com/guide/playcore/in-app-updates) to detect when an update is available
- This functionality by default checks the build number and not the version number, so you must make sure to continue to increase your build number even after increasing you version number.
- This plugin may be updated in the future to handle both the version number and the build number, but since this plugin is built around the in-app update functionality it may take a while.
- [__`needsUpdate`__](#example) will return either 1 or 0 when successful, which can still evaluate as booleans, so there is no need to handle Android differently from iOS.


## Requirements ##

### iOS

- No requirements, the plugin should work after [installing](#installing-the-plugin).

### Android

- Add ```implementation 'com.google.android.play:core:1.6.3'``` to your dependencies in your build.gradle file
- Make sure you are using Java 1.8 or greater. In your build.gradle file look for *compileOptions* and verify your version matches or is greater than what is shown below:
```
compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
}
```

## Using the plugin ##

- The plugin creates the object [**`AppUpdate`**](#example).


## Installing the plugin ##

- ### NPM

```bash
npm install --save cordova-plugin-native-app-update
```

- ### Ionic

```bash
ionic cordova plugin add https://github.com/kungfu-king-betty/cordova-plugin-native-app-update.git
```

- ### Pure Cordova

```bash
cordova plugin add https://github.com/kungfu-king-betty/cordova-plugin-native-app-update.git
```

- ### PhoneGap CLI

```bash
phonegap plugin add https://github.com/kungfu-king-betty/cordova-plugin-native-app-update.git
```

- ### PhoneGap Build

```xml
<plugin name="cordova-plugin-native-app-update" spec="https://github.com/kungfu-king-betty/cordova-plugin-native-app-update.git" />
```


## Example ##

### Pure Cordova Javascript (eg: PhoneGap, Cordova, Ionic 1)
```javascript
onDeviceReady: function() {
  AppUpdate.needsUpdate(function(has_update){
      console.log("Update Available:",has_update);
  },function(error){
      console.log("App Update ERROR:",error);
  });
}
```


## TODO Items

### iOS

- Add functionality to allow update check between build numbers and version numbers

### Android

- Add functionality to allow update check between build numbers and version numbers
- Add scripting to automatically include the play core library
- Add scripting to check and automatically update the users java source to atleast 1.8
- Add functionality to check if the update is required or flexible
- Add functionality to allow the update to begin and download right in the app


## License ##

The MIT License

Copyright (c) 2020 [Austen Zeh](https://www.linkedin.com/in/austen-zeh-20bb55128/)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
