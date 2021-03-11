Changelog
=========

## Version 3.0.0

_2021-03-11_

- use [Thrifty 2.1.2](https://github.com/microsoft/thrifty/blob/master/CHANGELOG.md)
- use Okio 2.10.0, OkHttp 4.9.1, Kotlin 1.4.30
- migrate deployment to Maven Central
- change group id `co.infinum` -> `com.infinum`
- change package name `co.infinum` -> `com.infinum`

## Version 2.0.0

_2020-06-01_

- use [Thrifty 2.0.1](https://github.com/microsoft/thrifty/blob/master/CHANGELOG.md)
- use Okio 2.6.0, OkHttp 4.7.2, Retrofit 2.9.0, Kotlin 1.3.71

Note that OkHttp raised the [Android minSDK to API 21 with version 3.13](https://cashapp.github.io/2019-02-05/okhttp-3-13-requires-android-5). If you need an older version than is referenced by this library excluding the transitive dependency and including your own okhttp dependency should work just fine.

## Version 1.0.0

_2019-05-31_

- use Thrifty 1.0.0
- use Okio 2.2.2, OkHttp 3.14.2, Retrofit 2.5.0
- add Thrifty Kotlin runtime, along with tests for Kotlin (de)serialization

## Version 0.9.1

_2018-01-09_

- use Thrifty 0.4.3
- add support for Thrift JSON Protocol

## Version 0.9.0

_2017-07-18_

Initial release.
