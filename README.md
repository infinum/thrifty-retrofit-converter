# Thrifty Retrofit converter

[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/infinum/android/retrofit-converter-thrifty/images/download.svg) ](https://bintray.com/infinum/android/retrofit-converter-thrifty/_latestVersion)

A `Retrofit converter` implementation using [Thrifty](https://github.com/Microsoft/thrifty) for
(de)serialization of Apache Thrift requests and responses.

It supports `Binary`, `Compact` and `Json` Thrift protocols.
It supports Thrifty models generated in both Java and Kotlin.

## Usage

Add the library as a dependency to your `build.gradle` to automatically download it from jcenter.

```groovy
compile 'co.infinum:retrofit-converter-thrifty:1.0.0'
```

... and add the converter factory when you create the `Retrofit` object.

```java
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https://example.com/api/")
    .addConverterFactory(ThriftyConverterFactory.create(ProtocolType.COMPACT))
    .build();
```

We also maintain a [changelog](CHANGELOG.md).

## Generating new files with thrifty compiler

In case source files need to be generated from .thrift files using a different version of the thrifty compiler, use these commands:

```shell
java -jar thrifty-compiler-1.0.0-all.jar --out=retrofit-converter-thrifty/src/test/java/ --lang=java retrofit-converter-thrifty/src/test/resources/phone.thrift
java -jar thrifty-compiler-1.0.0-all.jar --out=retrofit-converter-thrifty/src/test/java/ --lang=kotlin --kt-file-per-type retrofit-converter-thrifty/src/test/resources/phone.thrift

```

## Deploying a new version

To deploy a new version to bintray, use `deploy.sh`:


```shell
./deploy.sh <bintray username> <bintray api key> <artifact version> [optional tag message]
```

## Contributing

Feedback and code contributions are very much welcome. Just make a pull request with a short description of your changes. By making contributions to this project you give permission for your code to be used under the same [license](LICENSE).
