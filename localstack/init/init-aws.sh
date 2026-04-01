#!/bin/bash
set -euo pipefail

echo ">> Creating a bucket and queue on localstack in a development environment."

BUCKET_NAME="lumio-bucket-development"
QUEUE_NAME="lumio-queue-development"

awslocal s3 mb "s3://$BUCKET_NAME"

echo ">> Bucket $BUCKET_NAME created successfully!"

QUEUE_URL=$(awslocal sqs create-queue --queue-name "$QUEUE_NAME" --output text)

echo ">> Queue $QUEUE_NAME created successfully!"
echo ">> Queue URL: $QUEUE_URL"