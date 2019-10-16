#!/bin/bash

# We need this to identify the build artefacts. E.g. docker image tag.
date "+%Y%m%d-%H%M%S" > ./buildtime

BUILD_TIMESTAMP=$(cat ./buildtime)
echo -e "Build environment variables:"
echo "REGISTRY_URL=${REGISTRY_URL}"
echo "REGISTRY_NAMESPACE=${REGISTRY_NAMESPACE}"
echo "IMAGE_NAME=${IMAGE_NAME}"
echo "BUILD_TIMESTAMP=$BUILD_TIMESTAMP"

echo -e "Checking for Dockerfile at the repository root"
if [ -f Dockerfile ]; then
   echo "Dockerfile found"
else
    echo "Dockerfile not found"
    exit 1
fi

echo -e "Building container image"
set -x
bx cr build \
    -t $REGISTRY_URL/$REGISTRY_NAMESPACE/$IMAGE_NAME:${BUILD_TIMESTAMP} \
    --build-arg SONAR_TOKEN=${SONAR_TOKEN} \
    --build-arg SONAR_HOST=${SONAR_HOST} \
    .
set +x

BUILD_NAME=$(echo "build-${BUILD_NUMBER}-${BUILD_TIMESTAMP}")
echo -e "Tagging git commit ${GIT_COMMIT} with build tag '${BUILD_NAME}'"
git tag ${BUILD_NAME}
git push origin ${BUILD_NAME}
