FROM openjdk:11

RUN apt-get update  && \
    apt-get install -y systemd iputils-ping telnet net-tools curl vim iproute2 dnsutils bind9 bind9-utils


LABEL maintainer="Li Rui <lirui10093622@163.com>"
LABEL author="Li Rui" version="1.0.0" description="用户服务提供者"
EXPOSE 20880
WORKDIR /
ADD target/emall-user-provider-1.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]