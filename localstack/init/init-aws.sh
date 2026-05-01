#!/bin/bash
set -euo pipefail

echo ">> Creating buckets for the development environment."

VIDEO_BUCKET="lumio-videos"
RESOURCE_BUCKET="lumio-resources"

echo ">> Creating buckets..."
awslocal s3 mb "s3://$VIDEO_BUCKET"
echo ">> Bucket $VIDEO_BUCKET created successfully!"

awslocal s3 mb "s3://$RESOURCE_BUCKET"
echo ">> Bucket $RESOURCE_BUCKET created successfully!"

echo ">> Development environment configuration created successfully."