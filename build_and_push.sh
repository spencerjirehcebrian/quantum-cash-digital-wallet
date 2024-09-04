#!/bin/bash

# Array of service names
services=("account-service" "bank-reconciliation-service" "notification-services" "payment-gateway-service" "transaction-service" "user-service")

# Iterate over each service
for service in "${services[@]}"; do
  echo "Processing $service..."

  # Navigate to the service directory
  cd $service || exit

  # Run the Maven package command
  mvn clean package -DskipTests

  # Build and push the Docker image
  docker buildx build --platform linux/amd64,linux/arm64 -t sjcebrian/${service}:latest --push .

  # Navigate back to the parent directory
  cd ..

  echo "$service processed successfully."
done

echo "All services processed."
