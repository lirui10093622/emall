FROM openjdk:11

RUN apt-get update  && \
    apt-get install -y systemd iputils-ping telnet net-tools curl vim iproute2 dnsutils bind9 bind9-utils


LABEL maintainer="Li Rui <lirui10093622@163.com>"
LABEL author="Li Rui" version="1.0.0" description="门面模块"
EXPOSE 8081
WORKDIR /
ADD target/emall-service-facade-1.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]