#!/bin/bash

gcloud version

if [ ! -d "$HOME/google-cloud-sdk/bin" ]
then
  rm -rf $HOME/google-cloud-sdk
  export CLOUDSDK_CORE_DISABLE_PROMPTS=1
  curl https://sdk.cloud.google.com | bash
fi

# Add gcloud to $PATH
PATH=$PATH:${HOME}/google-cloud-sdk/bin
source $HOME/google-cloud-sdk/path.bash.inc

# Install kubectl
sudo apt-get update && sudo apt-get install -y apt-transport-https
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -
echo "deb http://apt.kubernetes.io/ kubernetes-xenial main" | sudo tee -a /etc/apt/sources.list.d/kubernetes.list
sudo apt-get update
sudo apt-get install -y kubectl

gcloud version