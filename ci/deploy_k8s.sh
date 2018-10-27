#!/bin/bash

if [ $# -ne 3 ]; then
    echo -e "\e[31mThree arguments expected. Usage: $0 <GCLOUD_SERVICE_ACOUNT_JSON_FILE> <DB_USER> <DB_PASSWORD>\e[39m"
    echo -e "\e[31mExample: $0 service_account.json root pass\e[39m"
    exit 1
fi

DB_SERVICE_ACCOUNT_FILE=$1
GYMIO_DB_USER=$2
GYMIO_DB_PASSWORD=$3

# Provision
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