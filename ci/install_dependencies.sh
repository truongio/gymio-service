#!/bin/bash

gcloud version

if [ ! -d "$HOME/google-cloud-sdk/bin" ]
then
  export CLOUDSDK_CORE_DISABLE_PROMPTS=1
  curl https://sdk.cloud.google.com | bash;
fi

# Add gcloud to $PATH
source /home/travis/google-cloud-sdk/path.bash.inc

# Install kubectl
sudo apt-get update && sudo apt-get install -y apt-transport-https
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -
echo "deb http://apt.kubernetes.io/ kubernetes-xenial main" | sudo tee -a /etc/apt/sources.list.d/kubernetes.list
sudo apt-get update
sudo apt-get install -y kubectl

# Update gcloud
sudo apt-get --only-upgrade -y install kubectl google-cloud-sdk