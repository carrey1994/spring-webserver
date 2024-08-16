#!/bin/bash
echo "Running cluster"
DEFAULT=true;
YAML_FOLDER="infra/kubernetes/k3d"
NAMESPACE=dev

while getopts i flag
do
    case "${flag}" in
        i )
          DEFAULT=false;
    esac
done

# Install ingress-gateway or k3d-ingress
if [ "$DEFAULT" = true ]; then
    k3d cluster create dev-cluster --api-port 6443 -p "8080:80@loadbalancer" --agents 1
    kubectl create namespace "$NAMESPACE"
    kubectl apply -f "$YAML_FOLDER/k3d-ingress.yml";
else
    k3d cluster create dev-cluster --api-port 6443 -p '8080:80@loadbalancer' -p '8443:443@loadbalancer' --agents 1 --k3s-arg '--disable=traefik@server:*'
    istioctl install -y;
    kubectl create namespace "$NAMESPACE"
    kubectl label namespace "$NAMESPACE" istio-injection=enabled;
    kubectl apply -f "$YAML_FOLDER/virtual-service-gateway.yml"
fi

# Check if the folder exists
if [ ! -d "$YAML_FOLDER" ]; then
    echo "Error: The specified folder does not exist."
    exit 1
fi

ingress=("$YAML_FOLDER/k3d-ingress.yml" "$YAML_FOLDER/virtual-service-gateway.yml")

# Loop through each YAML file in the folder
for file in "$YAML_FOLDER"/*.yml; do
    if [[ ! ${ingress[*]} =~ $file ]]; then
        echo "Applying $file...";
        kubectl apply -f "$file";
        printf "\n";
    fi
done

echo "All YAML files applied successfully."