# Used Eclipse Temurin 21 as the base image
FROM eclipse-temurin:21

# Install python3.10 pip venv and development tools
RUN apt-get update && apt-get install -y software-properties-common \
    && add-apt-repository ppa:deadsnakes/ppa \
    && apt-get update \
    && apt-get install -y \
        python3.10 python3.10-venv python3.10-distutils python3.10-dev build-essential \
    && ln -sf /usr/bin/python3.10 /usr/bin/python3 \
    && curl https://bootstrap.pypa.io/get-pip.py | python3

# Set the working directory to /app directory
WORKDIR /app

# Copy the Maven Wrapper files and project files to the container
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd
COPY .mvn/ .mvn/
COPY pom.xml .

# Copy all the sub-modules
COPY Recipes-common/ Recipes-common/
COPY Recipes-pojo/ Recipes-pojo/
COPY Recipes-server/ Recipes-server/

# Build the Maven project, skipping the test phase
RUN ./mvnw clean package -DskipTests

# Copy the python dependencies and create a new virtual environment
COPY requirements.txt /app/requirements.txt

# Create the virtual environment and install all dependencies, skipping scikit-surprise
RUN python3 -m venv /app/venv \
    && /app/venv/bin/pip install --upgrade pip \
    && /app/venv/bin/pip install setuptools wheel cython \
    && grep -v "scikit-surprise" /app/requirements.txt > /app/requirements_no_surprise.txt \
    && /app/venv/bin/pip install -r /app/requirements_no_surprise.txt

# Install scikit-surprise and its dependencies
RUN /app/venv/bin/pip install scikit-surprise

# Expose port 8085 for the application to serve externally
EXPOSE 8085

# Start the Spring Boot application, the main module is Recipes-server, run the generated JAR file
CMD ["java", "-jar", "/app/Recipes-server/target/Recipes-server-1.0-SNAPSHOT.jar"]
