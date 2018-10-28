#!/bin/bash

if [ $# -ne 1 ]; then
    echo -e "\e[31mOne arguments expected: service account file\e[39m"
    echo -e "\e[31mExample: $0 service_account.json\e[39m"
    exit 1
fi

GOOGLE_PROJECT_ID="gymio-220023"
GOOGLE_COMPUTE_ZONE="europe-north1-a"
SERVICE_ACCOUNT_FILE=$1

gcloud auth activate-service-account --key-file=${SERVICE_ACCOUNT_FILE}
gcloud --quiet config set project ${GOOGLE_PROJECT_ID}
gcloud --quiet config set compute/zone ${GOOGLE_COMPUTE_ZONE}
gcloud container clusters get-credentials gymio-cluster

docker pull alpine:latest
docker tag alpine:latest gcr.io/gymio-220023/gymio-service:test-123
gcloud docker -- push gcr.io/gymio-220023/gymio-service:test-123