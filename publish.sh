#!/usr/bin/env bash

######################################################################################################
#
# This deploy script requires have all the necessary gradle properties already set up:
# - signing.keyId
# - signing.password
# - signing.secretKeyRingFile
# - sonatype.url
# - sonatype.username
# - sonatype.password
#
######################################################################################################

# Stop script on errors or undefined vars
set -euo pipefail;

readonly SCRIPT_NAME="$(basename "$0")"
readonly green="\e[0;32m"
readonly reset="\e[0m"

if [ $# -lt 1 ]; then
    echo
    echo "Usage:"
    echo -e "${green}$SCRIPT_NAME <version> [optional tag message]${reset}"
    exit 1
fi

readonly version="$1"

# default tag message
tag_message="$version release"

if [ -n "${2-}" ]; then
    tag_message="$2"
fi

echo -e "${green}Building and uploading release ...${reset}"
./gradlew clean build publishLibraryPublicationToSonatypeRepository

echo -e "${green}Pushing version tag ...${reset}"
git tag -a "${version}" -m "${tag_message}" && \
git push --tags

# fix version in README.md if needed
echo -e "${green}Setting version in README.md to $version ...${reset}"
sed -E "s/\"com.infinum:retrofit-converter-thrifty:[\.0-9]+\"/\"com.infinum:retrofit-converter-thrifty:$version\"/g" README.md > tmp_readme && mv tmp_readme README.md

# show changes so we know to commit if needed
echo -e "${green}\nHere's the output of git status:\n${reset}"
git status