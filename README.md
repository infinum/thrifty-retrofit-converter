# Thrifty Retrofit converter

[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/infinum/android/retrofit-converter-thrifty/images/download.svg) ](https://bintray.com/infinum/android/retrofit-converter-thrifty/_latestVersion)

A `Retrofit converter` implementation using [Thrifty](https://github.com/Microsoft/thrifty) for
(de)serialization of Apache Thrift requests and responses.

It supports `Binary` and `Compact` Thrift protocols.

## Usage

Add the library as a dependency to your `build.gradle` to automatically download it from jcenter.

```groovy
compile 'co.infinum:retrofit-converter-thrifty:0.9.0'
```

... and add the converter factory when you create the `Retrofit` object.

```java
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https:your.server.com/api/")
    .addConverterFactory(ThriftyConverterFactory.create(ProtocolType.COMPACT))
    .build();
```

We also maintain a [changelog](CHANGELOG.md).

## Deploying a new version

To deploy a new version to bintray, use `deploy.sh`:


```shell
./deploy.sh <bintray username> <bintray api key> <artifact version> [optional tag message]
```

## Contributing

Feedback and code contributions are very much welcome. Just make a pull request with a short description of your changes. By making contributions to this project you give permission for your code to be used under the same [license](LICENSE).
