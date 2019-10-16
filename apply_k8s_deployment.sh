#!/bin/bash

# Make sure the cluster is running and get the ip_address 

ip_addr=$(bx cs workers $PIPELINE_KUBERNETES_CLUSTER_NAME | grep normal | awk '{ print $2 }')
if [ -z $ip_addr ]; then
  echo "$PIPELINE_KUBERNETES_CLUSTER_NAME not created or workers not ready"
  exit 1
fi

# Initialize script variables
export CLUSTER_NAME_LOWERCASE=$(echo ${PIPELINE_KUBERNETES_CLUSTER_NAME} | tr '[:upper:]' '[:lower:]')
export BUILD_TIMESTAMP=$(cat ./buildtime)
echo ""
echo "Deploy environment variables:"
echo "IMAGE=$IMAGE"
echo "BUILD_TIMESTAMP=$BUILD_TIMESTAMP"
echo ""

DEPLOYMENT_FILE="deployment.yml"
echo "Creating deployment manifest $DEPLOYMENT_FILE"

# Build the deployment file and replace environment variables
# with current values.
envsubst < ./k8s/deployment.yml > $DEPLOYMENT_FILE

# Show the file that is about to be executed
echo ""
echo "DEPLOYING USING MANIFEST:"
echo "***"
cat $DEPLOYMENT_FILE
echo "***"

# Execute the file
echo "KUBERNETES COMMAND:"
echo "kubectl apply -f $DEPLOYMENT_FILE"
kubectl apply -f $DEPLOYMENT_FILE
echo ""
