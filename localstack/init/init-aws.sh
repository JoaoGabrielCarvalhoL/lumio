#!/bin/bash
set -euo pipefail

echo ">> Creating buckets and queues for the development environment."

VIDEO_BUCKET="lumio-videos"
RESOURCE_BUCKET="lumio-resources"

echo ">> Creating buckets..."
awslocal s3 mb "s3://$VIDEO_BUCKET"
echo ">> Bucket $VIDEO_BUCKET created successfully!"

awslocal s3 mb "s3://$RESOURCE_BUCKET"
echo ">> Bucket $RESOURCE_BUCKET created successfully!"

VIDEO_DLQ_QUEUE="lumio-videos-dlq"
RESOURCE_DLQ_QUEUE="lumio-resources-dlq"
USER_CREATE_DLQ_QUEUE="lumio-user-create-dlq"

echo ">> Creating DLQs..."
awslocal sqs create-queue --queue-name "$VIDEO_DLQ_QUEUE"
awslocal sqs create-queue --queue-name "$RESOURCE_DLQ_QUEUE"
awslocal sqs create-queue --queue-name "$USER_CREATE_DLQ_QUEUE"
echo ">> DLQs criadas com sucesso!"

VIDEO_DLQ_ARN="arn:aws:sqs:us-east-1:000000000000:$VIDEO_DLQ_QUEUE"
RESOURCE_DLQ_ARN="arn:aws:sqs:us-east-1:000000000000:$RESOURCE_DLQ_QUEUE"
USER_CREATE_DLQ_ARN="arn:aws:sqs:us-east-1:000000000000:$USER_CREATE_DLQ_QUEUE"

VIDEO_QUEUE="lumio-videos-upload"
RESOURCE_QUEUE="lumio-resources-upload"
USER_CREATE_QUEUE="lumio-user-create"

echo ">> Creating main queues..."
awslocal sqs create-queue --queue-name "$VIDEO_QUEUE"
awslocal sqs create-queue --queue-name "$RESOURCE_QUEUE"
awslocal sqs create-queue --queue-name "$USER_CREATE_QUEUE"

VIDEO_QUEUE_URL="http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/$VIDEO_QUEUE"
RESOURCE_QUEUE_URL="http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/$RESOURCE_QUEUE"
USER_CREATE_QUEUE_URL="http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/$USER_CREATE_QUEUE"


echo ">> Associating DLQs with main queues..."

awslocal sqs set-queue-attributes \
  --queue-url "$VIDEO_QUEUE_URL" \
  --attributes '{
    "RedrivePolicy": "{\"deadLetterTargetArn\":\"'"$VIDEO_DLQ_ARN"'\",\"maxReceiveCount\":\"5\"}"
  }'

awslocal sqs set-queue-attributes \
  --queue-url "$RESOURCE_QUEUE_URL" \
  --attributes '{
    "RedrivePolicy": "{\"deadLetterTargetArn\":\"'"$RESOURCE_DLQ_ARN"'\",\"maxReceiveCount\":\"5\"}"
  }'

awslocal sqs set-queue-attributes \
  --queue-url "$USER_CREATE_QUEUE_URL" \
  --attributes '{
    "RedrivePolicy": "{\"deadLetterTargetArn\":\"'"$USER_CREATE_DLQ_ARN"'\",\"maxReceiveCount\":\"5\"}"
  }'

echo ">> Main queues created and DLQs successfully associated."
echo ">> Development environment configuration created successfully."