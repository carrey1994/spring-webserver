#!/bin/bash
for i in {64..100}
do
   curl --location '127.0.0.1:8080/api/v1/user/management/add' \
   --header 'Content-Type: application/json' \
   --header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJ1c2VyX2lkIjoiMSIsImV4cCI6MTcwNzg1Mjg4NywiaWF0IjoxNzA3ODUyMjg3LCJpc3MiOiJ4VmNxcXpMUlVTbllVS2dBY2lQS25BcWdySEdwRExtbkVpdUxYSGVIcUJpRkhKcFEifQ.zfPl_ySfGDIosIPzlaL8UqvwKD4EMRVrYJfCIEc1Ia1jcoBjgjTTVxkqsG-2YSm8' \
   --data-raw '{
    "username": "testuser$i",
    "password": "testuser$i",
    "email": "testusert$i@gmail.com",
    "address": "Taipei",
    "date": "2024-01-23",
    "recommenderId": "1"
}'
done
