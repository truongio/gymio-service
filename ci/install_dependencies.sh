#!/bin/bash

docker version

if [ ! -d "$HOME/google-cloud-sdk/bin" ]
then
  # Remove old travis SDK
  sudo rm -rf /usr/lib/google-cloud-sdk
  # Remove old/wrong cache
  sudo rm -rf $HOME/google-cloud-sdk
  export CLOUDSDK_CORE_DISABLE_PROMPTS=1
  curl https://sdk.cloud.google.com | bash
  gcloud --quiet components update kubectl
fi

