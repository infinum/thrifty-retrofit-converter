# Thrifty Retrofit converter

[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

A `Retrofit converter` implementation using [Thrifty](https://github.com/Microsoft/thrifty) for
(de)serialization of Apache Thrift requests and responses.

It supports `Binary`, `Compact` and `Json` Thrift protocols.
It supports Thrifty models generated in both `Java` and `Kotlin`.

## Usage

Add the library as a dependency to your `build.gradle` to automatically download it from Maven Central.

```groovy
implementation "com.infinum:retrofit-converter-thrifty:3.0.0"
```

... and add the converter factory when you create the `Retrofit` object.

```java
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https://example.com/api/")
    .addConverterFactory(ThriftyConverterFactory.create(ProtocolType.COMPACT))
    .build();
```

We also maintain a [changelog](CHANGELOG.md).

## Contributing

Feedback and code contributions are very much welcome. Just make a pull request with a short description of your changes. By making contributions to this project you give permission for your code to be used under the same [license](LICENSE).

### Generating new files with the thrifty compiler

In case source files need to be generated from .thrift files using a different version of the thrifty compiler, use these commands:

```shell
wget https://repo1.maven.org/maven2/com/microsoft/thrifty/thrifty-compiler/2.1.2/thrifty-compiler-2.1.2-all.jar
java -jar thrifty-compiler-2.1.2-all.jar --out=retrofit-converter-thrifty/src/test/java/ --lang=java retrofit-converter-thrifty/src/test/resources/phone.thrift
java -jar thrifty-compiler-2.1.2-all.jar --out=retrofit-converter-thrifty/src/test/kotlin/ --lang=kotlin --kt-file-per-type retrofit-converter-thrifty/src/test/resources/phone.thrift
```

### Deploying a new version

To publish  to Maven Central you first need to set the following [Gradle properties](https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_configuration_properties):

```
signing.keyId=<GPG_KEY_ID>
signing.password=<GPG_KEY_PASSWORD>
signing.secretKeyRingFile=<GPG_SECRING_LOCATION>

sonatype.url=https://oss.sonatype.org/service/local/staging/deploy/maven2/
sonatype.username=<SONATYPE_USERNAME>
sonatype.password=<SONATYPE_PASSWORD>
```

To deploy a new version to Maven Central, use `deploy.sh`:

```shell
./publish.sh <artifact version> [optional tag message]
```

The publish script will automatically push a tag with the version name and change the version in the README.
