#!/bin/bash
for i in {1..15}
do
   curl --location '127.0.0.1:8080/health-checker/test' --header 'Authorization: Basic YW5kcm9pZHg6YW5kcm9pZHg=' --header 'Cookie: Cookie_1=value'
done
