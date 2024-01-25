#!/bin/bash
for i in {1..50}
do
#   echo "Welcome $i times"
   curl --location 'localhost:8080/health-checker/test' --header 'Authorization: Basic YW5kcm9pZHg6YW5kcm9pZHg=' --header 'Cookie: Cookie_1=value' &
done
