#!/bin/bash
echo "Running cluster"
k3d cluster create dev-cluster --api-port 6443 -p "8081:80@loadbalancer" --agents 2
kubectl create namespace dev

#echo "Building image"
#docker build --no-cache -t carrey1994/spring-webserver -f docker/Dockerfile .

YAML_FOLDER="kubernetes"

# Check if the folder exists
if [ ! -d "$YAML_FOLDER" ]; then
  echo "Error: The specified folder does not exist."
  exit 1
fi

# Loop through each YAML file in the folder
for file in "$YAML_FOLDER"/*.yml; do
  echo "Applying $file..."
  kubectl apply -f "$file"
  printf "\n"
done

echo "All YAML files applied successfully."