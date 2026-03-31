#!/bin/bash
set -e

echo ">> Initializing LocalStack resources..."

awslocal s3 mb s3://lumio-bucket || true

echo ">> Done!"