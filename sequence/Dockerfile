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
