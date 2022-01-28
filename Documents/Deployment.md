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
application.properties를 다음과 같이 수정.

```
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://springbootwebservice.cbslkzqfwt2i.ap-northeast-2.rds.amazonaws.com:3306/CaptureTheSequence
spring.datasource.username=
spring.datasource.password=
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=create
spring.jpa.properties.hibernate.format_sql=true
```
```bash
$ docker run --network host cts
$ docker run -p 8080:8080 cts

# 모두 아래와 같은 문제 발생
# com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure
# The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.

$ gradlew clean
$ gradlew.bat build
$ docker build -t cts .
$ docker run -p 8080:8080 cts 

# clean build를 하니 문제 해결.
```

# ECR에 Docker image 올리기

710847041495.dkr.ecr.ap-northeast-2.amazonaws.com/cts_container 라는 이름으로 ECR 생성.

```bash
#  View push commands -> Push commands for cts_container
$ sudo apt-get update
$ sudo apt-get install awscli
$ aws --version

$ aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 710847041495.dkr.ecr.ap-northeast-2.amazonaws.com

$ docker tag cts:latest 710847041495.dkr.ecr.ap-northeast-2.amazonaws.com/cts_container:latest

$ docker push 710847041495.dkr.ecr.ap-northeast-2.amazonaws.com/cts_container:latest

# EC2
$ sudo yum -y upgrade
$ sudo yum -y install docker
$ docker -v

$ sudo systemctl status docker
$ sudo systemctl start docker

$ sudo yum install awscli
$ sudo yum install amazon-ecr-credential-helper
$ mkdir .docker
$ nano ~/.docker/config.json

{
  	"auths" : {
		"credHelpers": {
			"public.ecr.aws": "ecr-login",
			"710847041495.dkr.ecr.ap-northeast-2.amazonaws.com": "ecr-login"
		}
    }
}



$ aws config 
$ aws ecr get-login-password --region ap-northeast-2

$ sudo docker login -u AWS -p eyJwYXlsb2FkIjoiU013Z1c5MVRUbE5QbzZBQlh4cVMyTEVaM2w3eGxrakwvSGNIakZBVlhCODgxSG9SYVJEdTQ0U2ZzbnRUZkN6VEhyVHA0SmdSVGY3N084N21nSE9LVXNHOGlNaWVGU0J5TmdBNEtqNFdGOUN3YXhDaHNEcVE4U1Vpd1JtOWxQckpGaFVXMzRMMXl6ZUpXaGx0N2k1NTZlelliMWtQZzlsbFJKdC95OFJlRFBOd085M09KM1dxUU82QlhiWjg3Um1rTWc2dlRqUmlhZDVxWTR2Vkt3MFZqUFBnMzlkelJ1VTVvVkM2YmVTd0YvRW9XVWlqU3NhSkd6Sy94bEtueDhlUCtWYlQwczVyTW5WV3VWVmQ1cjJGZU9tMUx3RG5FeG1vdjNqYk9YZk5XOWJ5bXNVb2JDNXFHVkZSanV3cVNxSEdPOW43MWZJNzJqL0IzbjNXTGdlemFiRlV2QzhETlRlSXZZa3A4SGx2ejFrNjJ5SVRNNGJ4QU9JV1NVbjVqUEJrVUtvdGZMNXdPVUNzTDdpbVBudTNOZWJOSUw3bEZwMVdjTW9MZjdrQUxzT0lBOW9nejFKSlVjOUo1U1dlZklDUVpSbVY5U3N2clFZS3o0NGhpRjFpUzNUUHNlWUdLaXZ5am90d0VxTE5SU1hUS2dWR0tQNC96VWRPWG54eWNrcHFvb2RlSHVpRm1kSlFnSVRZMmxrWEZ6d0VtRWF4M3VxVlBWeXgvbG1nUGJBL1hUR1BwNW1ieUIzdXIxTTF5eGNVVDVORENvSGNSQXBLSjFvN2w1R2N4QUs0YllmVTVFMFRlTjJxNjlpTUsvc0VLcUdYZVY5bms2M2g3NTNVWXhqQWo0YVk0amtyZEcxeEJhWGFYZlJsMWxHcVo1Vm8ybTdvWmZTWEcwalNqWFZVdTJJK1JTOFVDelcyQVZNQnhQdGhNQnp1a2xJcEwwYUdtTkxLTWJEdG9EcXlUWW5ZcXFOOFQyRVIwQ3dYTTVFSUxiTXplVmFGdXFNVFpEaTB5TUFKcGhRMHl4V3BKU1JkbkZEejQ4MndiZnFZT1Z0QVMyUTBqZzNhS2VCTUdFemVvQ0lNbUhYcmVkdlF2dTJiTG9URUs4SnJoNjZZbjlNWklJZ09MQi9FYW8vVzc4Rzd1TXM2dE5ITEN3VmUzZHhLZi9tbVQvZ3ZOQzZYY0FVblNObjdwTlNYejlEVTZnOStXdFM3Wk1FV2g1bWlUZ2tMQ0VhM0tqcjBNREdrM0w0S3FnYlQ1UnFybHJxTVlpdEFCWm5HWDRHT0V5ZlptZ2VqTmc1T0RrY1hhQW54OTI0TC90VHp5VWdUb3QybXVTTjhZTVk0VHZINFZlcFgyWUFNYlhScXpuNVJ6MlJsVkJDaGhBNEdzcXFqY1hobWhPOEl6WjBxK0dSNi9tNXFUVnFoZG5QSmkzRGs5RnM9IiwiZGF0YWtleSI6IkFRSUJBSGhBT3NhVzJnWk4wOVdOdE5Ha1ljOHFwMTF4U2haL2RyRUVveTFIazhMWFdnR29UQ3A1OUF5MVdZT3lqK1luU0dNSkFBQUFmakI4QmdrcWhraUc5dzBCQndhZ2J6QnRBZ0VBTUdnR0NTcUdTSWIzRFFFSEFUQWVCZ2xnaGtnQlpRTUVBUzR3RVFRTVpPNVAwWkxlbW15Vjl1T3BBZ0VRZ0R2eDU0VWFQaHlIMnRyU3NBTk9QeEZhTWlxb0puNW5HbXFyblR6aGNxLytFdXBHbnhmQzNvNWJycU1YK0dtVm01WVQxV255T2ZLUlNOV2VrZz09IiwidmVyc2lvbiI6IjIiLCJ0eXBlIjoiREFUQV9LRVkiLCJleHBpcmF0aW9uIjoxNjQyNTk5MzY0fQ== 710847041495.dkr.ecr.ap-northeast-2.amazonaws.com


$ sudo docker pull 710847041495.dkr.ecr.ap-northeast-2.amazonaws.com/cts_container:latest

$ sudo docker run -p 8080:8080 8159f6bff7b9

# http://52.78.42.7:8080/strategy/getStrategies [GET]

```

# Dockerizing React app

```bash
# .dockerignore

.git
.gitignore
/node_modules
Dockerfile
README.md


# Dockerfile

# pull the official base image  
FROM node:16.13.0-alpine
 
# set your working directory  
WORKDIR /app  
 
# add `/app/node_modules/.bin` to $PATH  
ENV PATH /app/node_modules/.bin:$PATH  
 
# install application dependencies  
COPY package.json ./  
COPY package-lock.json ./  
RUN npm install
RUN npm install react-scripts@3.0.1 -g
 
# add app  
COPY . ./  
 
# will start app  
CMD ["npm", "start"]
```

```bash
$ docker build -t react_cts .
$ docker run -p 8081:3000 --rm react_cts
```

public.ecr.aws/i5j0h7d2/react_cts ECR 생성.

```bash
# Push commands for react_cts
$ aws ecr-public get-login-password --region us-east-1 | docker login --username AWS --password-stdin public.ecr.aws/i5j0h7d2
$ docker tag react_cts:latest public.ecr.aws/i5j0h7d2/react_cts:latest
$ docker push public.ecr.aws/i5j0h7d2/react_cts:latest

# EC2
$ sudo systemctl status docker
$ sudo systemctl start docker
$ sudo docker pull public.ecr.aws/i5j0h7d2/react_cts

# https://www.daleseo.com/docker-run/
$ sudo docker container ps -a
$ sudo docker start jolly_noyce
$ sudo docker run -d -p 3000:3000 1dc50704d2c2
$ sudo docker container ps
```

React app을 위한 EC2 인스턴스 생성. EIP 등록. 
```bash
# react-webservice.pem를 ~/.ssh 로 복사
$ cd ~/.ssh
$ chmod 600 ./react-webservice.pem
$ nano config

#####################################################
########## springboot-webservice
Host springboot-webservice
     HostName 52.78.42.7
     User ec2-user
     IdentityFile ~/.ssh/springboot-webservice.pem

######################################################
########## react-webservice
Host react-webservice
     HostName 3.35.254.243
     User ec2-user
     IdentityFile ~/.ssh/react-webservice.pem

$ ssh react-webservice
```
