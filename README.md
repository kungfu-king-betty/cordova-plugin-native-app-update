cordova-plugin-native-app-update &middot; [![npm](https://img.shields.io/npm/dm/cordova-plugin-native-app-update.svg)]() [![npm](https://img.shields.io/npm/v/cordova-plugin-native-app-update.svg)]()
==============================================================================

By [**Austen Zeh**](https://www.linkedin.com/in/austen-zeh-20bb55128/)

------------------------------------------------------------------------------

AppUpdate is a simple plugin for iOS and Android which will look for an app update through the App Store and Google Play Store respectively. This plugin was *built to assist with the inconsistent review times by both Apple and Google* after rolling out a new release version to production. This may also allow you to roll out a new version release for one OS, but not the other.

This plugin is defined as native because the app information is pulled right from the application itself using the native OS. Since the app information is gathered by the plugin, there is **no need to provide an appID**. However, because the plugin must make a request to either of the stores, the app **will need a connection** to make the request, and the update process will have to wait for the success callback to be executed. For more information on this please look below at the examples.

AppUpdate provides a [__`needsUpdate`__](#example) method to check if an update is available in the OS store. This method returns a JSON object that will *always* contain **update_available** when the plugin executes successfully.

You are also able to provide an api url and api response key to make an api request to **check app update type for a forced update.** This api should be set up on your app server and should return a JSON object. The JSON object that is returned from the api response is appended to the object returned by the plugin, so you will have access to more than just the *update_available* flag.

:new: The Android version of the plugin has been updated to use the [**in-app updates**](https://developer.android.com/guide/playcore/in-app-updates) functionality, which is the recommended way to check for Android app updates.

### iOS

- Currently, for iOS devices, the plugin **checks the version number, not the build number**, so if you release multiple builds within the same version, then this plugin will not handle these updates properly. (ex. 1.0.0 != 1.0.1)
- This plugin may be updated in the future to handle multiple builds within the same version, but seeing as Apple requires a new version number for new releases this may not be necessary at all.
- **update_available** has a value of type *boolean*

### Android

- This plugin uses Android's [**in-app updates**](https://developer.android.com/guide/playcore/in-app-updates) functionality to check for app updates
- **update_available** has a value of 1 or 0 which will evaluate to a boolean, so Android does not need to be handled differently
- If no api response key is passed in, then **force_update** will be returned by the plugin also and the value will be determined using Android's in-app updates functionality


## Requirements ##

### iOS

- No requirements, the plugin should work immediately after [installing](#installing-the-plugin).

### Android

- All Android requirements are handled when [installing the plugin](#installing-the-plugin).


## Installing the plugin ##

- ### NPM

```bash
npm install --save cordova-plugin-native-app-update
```

- ### Ionic

```bash
ionic cordova plugin add cordova-plugin-native-app-update
```

- ### Pure Cordova

```bash
cordova plugin add cordova-plugin-native-app-update
```

- ### PhoneGap CLI

```bash
phonegap plugin add cordova-plugin-native-app-update
```

- ### PhoneGap Build

```xml
<plugin name="cordova-plugin-native-app-update" spec="https://github.com/kungfu-king-betty/cordova-plugin-native-app-update.git" />
```


## Using the plugin ##

- The plugin creates the [**`AppUpdate`**](#example) object, to used in your app
- Call the [**`needsUpdate`**](#example) method with a success handler and error handler
- (Optional) Call the [**`needsUpdate`**](#example) method with a success handler, error handler, force_api_url, force_api_response_key
- In the success handler, check the [**`update_available`**](#example) value to determine your next move
- In the success handler, you can also check the [**`force_update`**](#example) value to handle forced updates


## Example ##

### Pure Cordova Javascript (eg: Cordova, Ionic, PhoneGap)
```javascript
onDeviceReady: function () {
    // Example #1: WITHOUT API URL
    AppUpdate.needsUpdate(function(appUpdateObj) {
        if(appUpdateObj.update_available == 1) {
            // App Update Detected
            var appUpdateMsg = "App Update Detected";
            if (appUpdateObj.force_update == 1) {
                appUpdateMsg = `FORCE! ${appUpdateMsg}`;
            }
            
            alert(appUpdateMsg);
        } else {
            // NO App Update Detected
            alert("No App Update Available");
        }
    }, function(error){
        alert("App Update ERROR:",error);
    });
    
    // Example #2: WITH API URL
    AppUpdate.needsUpdate(function(appUpdateObj) {
        if(appUpdateObj.update_available == 1) {
            // App Update Detected
            var appUpdateMsg = "App Update Detected";
            if (appUpdateObj.force_update == 1) {
                appUpdateMsg = `FORCE! ${appUpdateMsg}`;
            }
            
            // Use custom key/values returned from the api call
            if (appUpdateObj.custom_force_key) {
                appUpdateMsg = "Custom App Force Update Available";
            }

            alert(appUpdateMsg);
        } else {
            // NO App Update Detected
            alert("No App Update Available");
        }
    }, function(error){
        alert("App Update ERROR:",error);
    }, "https://localhost/appupdatetest/test", "custom_force_key");
}
```


## TODO Items

- Add wiki/docs for the AppUpdate plugin
- **[Android]** Possibly updated the plugin to use Android's [**in-app updates**](https://developer.android.com/guide/playcore/in-app-updates) functionality to install the app update as well as check an available update


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
