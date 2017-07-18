#!/usr/bin/env bash
if [ $# -lt 3 ]; then
    echo "Usage: <bintray username> <bintray api key> <version> [optional tag message]"
    exit 1
fi

username="$1"
api_key="$2"
version="$3"

# default tag message
tag_message="$version release"

if [ ! -z "$4" ]; then
    tag_message="$4"
fi

./gradlew clean build generatePomFileForMavenPublication bintrayUpload -PbintrayUser=${username} -PbintrayKey=${api_key} -PdryRun=false && \
git tag -a "${version}" -m "${tag_message}" && \
git push --tags

# fix version in README.md if needed
sed -E "s/'co.infinum:retrofit-converter-thrifty:[\.0-9]+'/'co.infinum:retrofit-converter-thrifty:$version'/g" README.md > tmp_readme && mv tmp_readme README.md

# show changes so we know to commit if needed
git status