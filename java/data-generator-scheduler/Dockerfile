	# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Add Maintainer Info
LABEL maintainer="jose.miguel.usp@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8116 available to the world outside this container
EXPOSE 8116

# The application's jar file
ARG JAR_FILE=target/data-generator-scheduler-1.0-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} data-generator-scheduler-1.0-SNAPSHOT.jar

# Add bash 
RUN apk add --no-cache bash gawk sed grep bc coreutils

#Create logg directory
RUN mkdir -p /logs

# Run the jar file 
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/data-generator-scheduler-1.0-SNAPSHOT.jar"]
