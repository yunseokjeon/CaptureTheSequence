# EC2 보안 그룹 구성
- SSH / 프로토콜 TCP / 포트 범위 22 / 소스 내 IP
- HTTPS / 프로토콜 TCP / 포트 범위 443 / 소스 사용자 지정 0.0.0.0/0
- HTTP / 프로토콜 TCP / 포트 범위 80 / 소스 사용자 지정 0.0.0.0/0

8080, 3000번 포트는 Type에서 Custom TCP를 선택.

HTTPS, HTTP는 외부에서 서비스에 접근할 때 사용하는 유형이기 때문에 포트를 열어 놓는다.

# Elastic IP
AWS의 고정 IP를 Elastic IP라고 한다. 이를 발급받아 인스턴스에 연결한다.

# EC2 터미널 접속
다운 받은 키페어 pem 파일을 ~/.ssh 로 복사한다.
```bash
$ cp springboot-webservice.pem ~/.ssh
$ cd ~/.ssh
$ chmod 600 ./springboot-webservice.pem
$ nano config

#####################################################
########## springboot-webservice
Host springboot-webservice
     HostName 고정 IP
     User ec2-user
     IdentityFile ~/.ssh/springboot-webservice.pem

# Ubuntu를 선택했다면 기본 사용자가 ubuntu이며, 그 외에는 ec2-user

$ ssh springboot-webservice # 접속

# https://jojoldu.tistory.com/259?category=635883
```

# RDS 보안 그룹

생성한 DB에서 VPC security groups으로 들어가 보안 그룹 생성.

인바운드 규칙에서 아래 두 가지를 생성.

- 유형 MYSQL/Aurora / 프로토콜 TCP / 포트 범위 3306 / 소스 사용자 지정 / EC2의 보안 그룹 ID (엉뚱한 그룹 ID를 넣어서 헤맸는데 주의, EC2 서버와 RDS 간의 통신을 위함이다.)
- 유형 MYSQL/Aurora / 프로토콜 TCP / 포트 범위 3306 / 소스 내 IP / 현재 IP

위에서 생성한 보안 그룹 이름을 찾아서 보안 그룹 변경을 한다.

# 로컬 PC에서 RDS 접근

- Host: RDS의 Endpoint
- Username
- Password
- Port: 3306

# EC2에서 RDS 접근

```bash
$ ssh springboot-webservice

[ec2-user@ip ~]$ sudo yum install mysql # MySQL CLI 설치

[ec2-user@ip ~]$ mysql -u yun -p -h RDS의_Endpoint

MySQL [(none)]> show databases;

+--------------------+
| Database           |
+--------------------+
| CaptureTheSequence |
| information_schema |
| mysql              |
| performance_schema |
| sys                |
+--------------------+
5 rows in set (0.01 sec)
```

# Spring Boot Docker image

```
// application.properties

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://RDS의 Endpoint:3306/CaptureTheSequence?allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&serverTimezone=UTC
spring.datasource.username=
spring.datasource.password=
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true
```

build.gradle과 같은 위치에 Dockerfile 생성.

```Dockerfile
# Start with a base image containing Java runtime
#FROM java:11
FROM openjdk:11 as build

# Add Author info
LABEL maintainer="yunseok.jeon1877@gmail.com"

# Add a volume to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=build/libs/sequence-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} cts.jar

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/cts.jar"]

```

```bash
$ docker build -t cts .
$ docker run -p 8080:8080 cts
```

Dockerizing 없이 빌드하면 문제없이 작동하는데, 빌드한 도커 이미지를 실행하면 다음과 같은 에러가 발생함.

```
2022-01-13 10:48:19.844  INFO 1 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2022-01-13 10:48:20.935 ERROR 1 --- [           main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Exception during pool initialization.

com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
        at com.mysql.cj.jdbc.exceptions.SQLError.createCommunicationsException(SQLError.java:174) ~[mysql-connector-java-8.0.27.jar!/:8.0.27]

```



