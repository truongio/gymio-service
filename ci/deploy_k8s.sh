#!/bin/bash

if [ $# -ne 5 ]; then
    echo -e "\e[31m5 arguments expected. Usage: $0 <GCLOUD_SERVICE_ACOUNT_JSON_FILE> <DB_USER> <DB_PASSWORD> <BRANCH> <SHA>\e[39m"
    echo -e "\e[31mExample: $0 service_account.json root pass develop 123123\e[39m"
    exit 1
fi

DB_SERVICE_ACCOUNT_FILE=$1
GYMIO_DB_USER=$2
GYMIO_DB_PASSWORD=$3
REPOSITORY="gcr.io/gymio-220023/gymio-service"
BRANCH=$4
SHA=$5

# Provision
GYMIO_SERVICE_IMAGE=${REPOSITORY}:${BRANCH}-${SHA}
envsubst < deployment/gymio-deployment.yaml > deployment/gymio-deployment-rendered.yaml

# Create secrets
kubectl delete secret cloudsql-instance-credentials --ignore-not-found
kubectl create secret generic cloudsql-instance-credentials \
  --from-file=credentials.json=${DB_SERVICE_ACCOUNT_FILE}

kubectl delete secret cloudsql-db-credentials --ignore-not-found
kubectl create secret generic cloudsql-db-credentials \
  --from-literal=username="$GYMIO_DB_USER" --from-literal=password="$GYMIO_DB_PASSWORD"

# Deployment and service
kubectl apply -f deployment/gymio-deployment-rendered.yaml
kubectl apply -f deployment/gymio-service.yaml

# Cleanup
rm deployment/gymio-deployment-rendered.yaml