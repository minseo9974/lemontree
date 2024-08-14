#!/bin/bash

# 동시 실행할 요청 수
num_requests=1000

for i in $(seq 1 $num_requests)
do
   curl -X POST 'http://localhost:8080/payment/cancel' \
   -H 'Authorization: lemontree' \
   -H 'Content-Type: application/json' \
   -d '{"memberId": 1999, "orderId": 18}' &
done

# 동시 실행할 요청 수
num_requests=1000

for i in $(seq 1 $num_requests)
do
   curl -X POST 'http://localhost:8080/payment/cancel' \
   -H 'Authorization: lemontree' \
   -H 'Content-Type: application/json' \
   -d '{"memberId": 2000, "orderId": 19}' &
done

# 모든 백그라운드 작업이 완료될 때까지 대기
wait
