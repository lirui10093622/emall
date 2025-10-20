REM 项目路径
set DRIVER=D:
set EMALL_PROJECT_DIR=%DRIVER%\code\emall
set EMALL_INIT_DIR=%EMALL_PROJECT_DIR%\emall-init
set EMALL_MYSQL_IMAGE_BUILD_DIR=%EMALL_INIT_DIR%\emall-mysql-image-build
set EMALL_SERVICE_DIR=%EMALL_PROJECT_DIR%\emall-service

REM 创建网络
set EMALL_NETWORK=emall-network
docker network rm %EMALL_NETWORK%
docker network create %EMALL_NETWORK%

REM 切换磁盘
%DRIVER%

REM 中间件
cd %EMALL_MYSQL_IMAGE_BUILD_DIR%

REM docker stop mysql.middleware.emall.docker
REM docker rmi emall-middleware-mysql
REM docker build -t emall-middleware-mysql .
docker run -d --name mysql.middleware.emall.docker --network emall-network -p 3306:3306 -p 33060:33060 -e MYSQL_ROOT_PASSWORD=root -e TZ=Asia/Shanghai emall-middleware-mysql

REM docker stop zookeeper.middleware.emall.docker
REM docker rmi zookeeper.middleware.emall.docker
docker run -d --name zookeeper.middleware.emall.docker --network emall-network -p 2181:2181 ubuntu/zookeeper:latest

REM docker stop redis.middleware.emall.docker
REM docker rmi redis.middleware.emall.docker
docker run -d --name redis.middleware.emall.docker --network emall-network -p 6379:6379 redis:latest

docker run -d --name es.middleware.emall.docker -p 9200:9200 -p 9300:9300 -e discovery.type=single-node -e xpack.security.enabled=false -v D:/deploy/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml -v D:/deploy/elasticsearch/data:/usr/share/elasticsearch/data -v D:/deploy/elasticsearch/plugins:/usr/share/elasticsearch/plugins -d elasticsearch:9.1.3

docker run -d --name elasticsearch-head -p 9100:9100 mobz/elasticsearch-head:5

docker run -d --name kibana.middleware.emall.docker --network emall-network -p 5601:5601 -e ELASTICSEARCH_HOSTS=http://es.middleware.emall.docker:9200 -e xpack.security.enabled=false kibana:9.1.3

docker run -d --name zipkin.middleware.emall.docker --network emall-network -p 9410:9410 -p 9411:9411 openzipkin/zipkin:latest

REM 应用服务
ECHO 项目构建开始

cd %EMALL_SERVICE_DIR%\emall-service-cart
docker stop emall-service-cart
docker rmi emall-service-cart:1.0.0
docker build -t emall-service-cart:1.0.0 .
docker run -d --rm --name emall-service-cart --network emall-network -p 8085:8085 emall-service-cart:1.0.0

cd %EMALL_SERVICE_DIR%\emall-service-comment
docker stop emall-service-comment
docker rmi emall-service-comment:1.0.0
docker build -t emall-service-comment:1.0.0 .
docker run -d --rm --name emall-service-comment --network emall-network -p 8085:8085 emall-service-comment:1.0.0

cd %EMALL_SERVICE_DIR%\emall-service-order
docker stop emall-service-order
docker rmi emall-service-order:1.0.0
docker build -t emall-service-order:1.0.0 .
docker run -d --rm --name emall-service-order --network emall-network -p 8081:8081 emall-service-order:1.0.0

cd %EMALL_SERVICE_DIR%\emall-service-pay
docker stop emall-service-pay
docker rmi emall-service-pay:1.0.0
docker build -t emall-service-pay:1.0.0 .
docker run -d --rm --name emall-service-pay --network emall-network -p 8082:8082 emall-service-pay:1.0.0

cd %EMALL_SERVICE_DIR%\emall-service-product
docker stop emall-service-product
docker rmi emall-service-product:1.0.0
docker build -t emall-service-product:1.0.0 .
docker run -d --rm --name emall-service-product --network emall-network -p 8083:8083 emall-service-product:1.0.0

cd %EMALL_SERVICE_DIR%\emall-service-search
docker stop emall-service-search
docker rmi emall-service-search:1.0.0
docker build -t emall-service-search:1.0.0 .
docker run -d --rm --name emall-service-search --network emall-network -p 8084:8084 emall-service-search:1.0.0

cd %EMALL_SERVICE_DIR%\emall-service-sms
docker stop emall-service-sms
docker rmi emall-service-sms:1.0.0
docker build -t emall-service-sms:1.0.0 .
docker run -d --rm --name emall-service-sms --network emall-network -p 8086:8086 emall-service-sms:1.0.0

cd %EMALL_SERVICE_DIR%\emall-service-user
docker stop emall-service-user
docker rmi emall-service-user:1.0.0
docker build -t emall-service-user:1.0.0 .
docker run -d --rm --name emall-service-user --network emall-network -p 8087:8087 emall-service-user:1.0.0
REM docker run -d --rm --name emall-service-user --network emall-network -p 8087:8087 -e DUBBO_IP_TO_REGISTRY=10.10.17.96 emall-service-user:1.0.0

cd %EMALL_SERVICE_DIR%\emall-service-facade
docker stop emall-service-facade
docker rmi emall-service-facade:1.0.0
docker build -t emall-service-facade:1.0.0 .
docker run -d --rm --name emall-service-facade --network emall-network -p 80:80 emall-service-facade:1.0.0

ECHO 项目构建结束
