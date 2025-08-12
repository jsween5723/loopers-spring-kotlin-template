

go install go.k6.io/xk6/cmd/xk6@latest
xk6 build --with github.com/grafana/xk6-output-influxdb@latest

# 확인: 이 출력에 xk6-influxdb가 보여야 함
./k6 run -o help | grep -i xk6-influxdb
./k6 version

K6_INFLUXDB_ORGANIZATION=my-org \
K6_INFLUXDB_BUCKET=k6 \
K6_INFLUXDB_TOKEN=my-super-secret-token \
BASE_URL=http://localhost:8080 \
./k6 run -o xk6-influxdb=http://localhost:8086 ./test.js