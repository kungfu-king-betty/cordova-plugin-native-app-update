# CHANGELOG

## [2.0.0] &mdash; 2021-09-01
- [Added] Functionality to use In-App Updates Library for Android
- [Added] Helpful JSDoc style comments for the plugin and methods
- [Updated] Library used for making api requests for Android
- [Updated] README
- [Updated] ISSUE_TEMPLATE

## [1.0.3] &mdash; 2021-05-14
- [Fixed] Non-void function does not return a value in ios strict builds

## [1.0.2] &mdash; 2021-04-28
- [Updated] Android play core implementation in build.gradle

## [1.0.1] &mdash; 2020-03-21
- [Updated] README

## [1.0.0] &mdash; 2020-03-21
- [Removed] In-app updates check for Android so the plugin works on all Android devices
- [Added] Functionality to allow the user to check for if the updated is forced by adding a custom api call

## [0.4.1] &mdash; 2020-03-21
- [Updated] README

## [0.4.0] &mdash; 2020-03-21
- [Fixed] The Android source file to work for all android devices that use Google Play Services
- [Updated] The Android build file to include the necessary build requirements 
- [Updated] The Android build file to remove unnecessary requirements
- [Updated] The README to indicate the correct Android requirements and processing

## [0.3.1] &mdash; 2020-03-18
- [Fixed] Version requirement during Android installation

## [0.3.0] &mdash; 2020-03-18
- [Added] Functionality to allow the user to supply an api url to check for a forced update for iOS and Android

## [0.2.2] &mdash; 2020-03-17
- [Removed] compileOptions from build.gradle file for Android plugin

## [0.2.1] &mdash; 2020-03-17
- [Updated] build.grable for android plugin

## [0.2.0] &mdash; 2020-03-17
- [Added] Declaration to the android sources to automatically add the necessary implementation of the Google Play Store core library
- [Added] Declaration of Java 1.8 during the plugin install, so the user is no longer required to input this themselves
- [Removed] Requirements from README for android

## [0.1.1] &mdash; 2020-03-17
- [Fixed] Android source file to be compatible with cordova-android 8.1.0

## [0.1.0] &mdash; 2020-03-16
- [Added] npm install to README
- [Updated] npm package dependencies
- [Added] Library to npm

## [0.0.16] &mdash; 2020-03-16
- [Updated] LICENSE

## [0.0.15] &mdash; 2020-03-16
- [Updated] README

## [0.0.14] &mdash; 2020-03-16
- [Fixed] Broken links in README
- [Added] More links in README
- [Fixed] Issue in the CHANGELOG

## [0.0.13] &mdash; 2020-03-16
- [Updated] README

## [0.0.12] &mdash; 2020-03-16
- [Fixed] The plugin declaration for iOS

## [0.0.11] &mdash; 2020-03-16
- [Fixed] The iOS source file to create the object name properly

## [0.0.10] &mdash; 2020-03-16
- [Fixed] The error handler in the android source file to print the stack trace of the error

## [0.0.9] &mdash; 2020-03-16
- [Added] More info to the error handler in the android source file

## [0.0.8] &mdash; 2020-03-16
- [Fixed] Issues in the android source file that was causing nothing to be returned from the method
- [Fixed] The package name used in the plugin declaration that was causing an error during the class initialization

## [0.0.6] &mdash; 2020-03-16
- [Fixed] Multiple issues with the method call in the android source file which resulted in undefined being returned
- [Added] Error handler to the android source file

## [0.0.3] &mdash; 2020-03-16
- [Fixed] Declaration of method in Android source file
- [Fixed] Declaration of method in iOS source file

## [0.0.1] &mdash; 2020-03-16
- [Added] Initial commit to add functionality for iOS and Android
