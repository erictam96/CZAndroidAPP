<p align="center"><img width=12.5% src="https://github.com/erictam96/CZAndroidAPP/blob/master/app/src/main/res/drawable/foodfoxnologo.png"></p>
<p align="center" text> <b>Authentication form for Android </p>

<p align="center">
<img src="https://img.shields.io/pypi/status/Django.svg"/>
<a href="https://codeclimate.com/github/ChmaraX/logregform-android/maintainability"><img src="https://api.codeclimate.com/v1/badges/c3bd5593b9920bf9022a/maintainability"/></a>
<a href="https://github.com/ChmaraX/logregform-android/issues"><img src="https://img.shields.io/github/issues/ChmaraX/logregform-android.svg"/></a>
<img src="https://img.shields.io/badge/contributions-welcome-orange.svg"/>
<a href="https://github.com/ChmaraX/logregform-android/blob/master/LICENSE"><img src="https://img.shields.io/badge/license-MIT-blue.svg"></a>
</p>


## Basic Overview

> Android authentication form based on Firebase with few features, custom animation and design. 

## Features

- [x] Registration activity with email and password, also storing and username into database. 
- [x] Login activity with email/password, remembering logged user. 
- [x] Password reset activity (sending an email to user)
- [x] Wrong inputs errors
- [x] Welcome splash screen
- [x] Button gradients (on-click transitions)
- [ ] Social app login (not connected to API)



## Interface Preview 

<p align="center">
  <img src="https://firebasestorage.googleapis.com/v0/b/czphishingapp.appspot.com/o/Screenshot_20200203-150729_CZLogin.jpg?alt=media&token=3114d491-98c8-47f3-bd9e-2391723a282c" width="210"/>
  <img src="https://firebasestorage.googleapis.com/v0/b/czphishingapp.appspot.com/o/Screenshot_20200203-150740_CZLogin.jpg?alt=media&token=017e0983-6760-4037-bc15-fc4c1ef3e053" width="210"/>
  <img src="https://firebasestorage.googleapis.com/v0/b/czphishingapp.appspot.com/o/Screenshot_20200203-150744_CZLogin.jpg?alt=media&token=6fc2a36f-6fd0-41a9-8915-8f12dcfc48b2" width="210"/>



## Implementation

#### google-services.json
You need to set up your own [Firebase](https://firebase.google.com/) authenticator + database and download `google-services.json` file, then add it to `YourApp/app` folder:
