REM 创建网络
docker network create emall-network

REM 启动中间件容器
docker run -d --rm --name mysql.middleware.emall.docker     --network emall-network -p 3306:3306 -p 33060:33060 -e MYSQL_ROOT_PASSWORD=root mysql:latest

docker run -d --rm --name redis.middleware.emall.docker     --network emall-network -p 6379:6379 redis:latest

docker run -d --rm --name zookeeper.middleware.emall.docker --network emall-network -p 2181:2181 ubuntu/zookeeper:latest


REM 构建应用
docker build -t emall-user-provider:1.0.0 .

REM 启动应用容器
docker run -d --rm --name emall-user-provider --network emall-network -p 20880:20880 -e DUBBO_IP_TO_REGISTRY=10.10.17.96 emall-user-provider:1.0.0

docker run -d --rm --name emall-facade -p 80:80 --network emall-network emall-facade:1.0.0