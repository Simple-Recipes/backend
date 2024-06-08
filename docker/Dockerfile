# Use the official image as a parent image
FROM openjdk:23-jdk-slim

# 在docker容器中自建容器卷,用于数据保存和持久化工作
VOLUME /tmp

# Set the working directory in the container
WORKDIR /app

# Copy the built jar files from all submodules
COPY ../Recipes-server/target/*.jar app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

##运行过程中创建一个app.jar文件
#RUN sh -c 'touch /app.jar'
#ENV JAVA_OPTS=""

# Run the application
CMD ["java", "-jar", "app.jar"]
