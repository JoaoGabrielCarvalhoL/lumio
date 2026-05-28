#!/bin/bash
set -euo pipefail

echo ">> Creating buckets for the development environment."

VIDEO_BUCKET="lumio-videos"
RESOURCE_BUCKET="lumio-resources"
TRAILER_BUCKET="lumio-trailers"
THUMBNAIL_BUCKET="lumio-thumbnails"

LUMIO_S3_INFRA_QUEUE="lumio-s3-infra-queue"

echo ">> Creating buckets..."

awslocal s3 mb "s3://$VIDEO_BUCKET"
echo ">> Bucket $VIDEO_BUCKET created successfully!"

awslocal s3 mb "s3://$RESOURCE_BUCKET"
echo ">> Bucket $RESOURCE_BUCKET created successfully!"

awslocal s3 mb "s3://$TRAILER_BUCKET"
echo ">> Bucket $TRAILER_BUCKET created successfully!"

awslocal s3 mb "s3://$THUMBNAIL_BUCKET"
echo ">> Bucket $THUMBNAIL_BUCKET created successfully!"

echo ">> Creating a queue for upload notifications for the $VIDEO_BUCKET and $TRAILER_BUCKET buckets."

awslocal sqs create-queue --queue-name "$LUMIO_S3_INFRA_QUEUE"
QUEUE_ARN="arn:aws:sqs:us-east-1:000000000000:$LUMIO_S3_INFRA_QUEUE"

cat <<EOF > /tmp/s3-notification.json
{
  "QueueConfigurations": [
    {
      "QueueArn": "$QUEUE_ARN",
      "Events": ["s3:ObjectCreated:*"]
    }
  ]
}
EOF

echo ">> Linking S3 bucket notifications to SQS queue..."

awslocal s3api put-bucket-notification-configuration \
  --bucket "$VIDEO_BUCKET" \
  --notification-configuration file:///tmp/s3-notification.json

awslocal s3api put-bucket-notification-configuration \
  --bucket "$TRAILER_BUCKET" \
  --notification-configuration file:///tmp/s3-notification.json

echo ">> Notifications linked successfully to $LUMIO_S3_INFRA_QUEUE!"

echo ">> Development environment configuration created successfully."