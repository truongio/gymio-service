#!/bin/bash

IMAGE="gcr.io/gymio-220023/gymio-build-base"

if diff build.sbt $HOME/sbt-dependencies/build.sbt && diff project/plugins.sbt $HOME/sbt-dependencies/project/plugins.sbt
then
    echo "No changes in dependencies"
else
    echo "Rebuild dependencies docker"
    rm -rf $HOME/sbt-dependencies
    mkdir $HOME/sbt-dependencies
    mkdir $HOME/sbt-dependencies/project

    cp build.sbt $HOME/sbt-dependencies/build.sbt
    cp project/plugins.sbt $HOME/sbt-dependencies/project/plugins.sbt

    docker build -t ${IMAGE} -f build/base_build_image/Dockerfile .
    gcloud docker -- push ${IMAGE}
fi
