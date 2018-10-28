#!/bin/bash

if [ ! -d "$HOME/google-cloud-sdk/bin" ]
then
  sudo rm -rf /usr/lib/google-cloud-sdk
  sudo rm -rf $HOME/google-cloud-sdk
  export CLOUDSDK_CORE_DISABLE_PROMPTS=1
  curl https://sdk.cloud.google.com | bash
fi

gcloud --quiet components update kubectl
gcloud version