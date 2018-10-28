#!/bin/bash

curl -LO "https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl"
mkdir -p .bin; mv ./kubectl .bin/kubectl && chmod +x .bin/kubectl