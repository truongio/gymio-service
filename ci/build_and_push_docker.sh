#!/bin/bash

if [ $# -ne 2 ]; then
    echo -e "\e[31mTwo arguments expected: branch name and build number\e[39m"
    echo -e "\e[31mExample: $0 develop 123123\e[39m"
    exit 1
fi

REPOSITORY="gcr.io/gymio-220023/gymio-service"
BRANCH=$1
SHA=$2

# Pull the latest sbt compiled image
gcloud docker -- pull --no-cache gcr.io/gymio-220023/gymio-build-base:latest
# Build main docker image
docker build -t ${REPOSITORY}:${BRANCH} -t ${REPOSITORY}:${BRANCH}-${SHA} -f build/Dockerfile .
# Push
gcloud docker -- push ${REPOSITORY}:${BRANCH}
gcloud docker -- push ${REPOSITORY}:${BRANCH}-${SHA}