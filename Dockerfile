##todo: USER nonroot
# ----- Stage 1: Build the application using Maven
FROM maven:3.9.9-ibm-semeru-17-focal AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml ./

RUN mvn dependency:go-offline -B

# Copy the source code to the working directory
COPY . .

# Package the application (skip tests to speed up the build)
RUN mvn clean package -DskipTests

# ----- Stage 2: Run the application with a minimal JRE base image
FROM eclipse-temurin:17.0.14_7-jre-noble

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged JAR file from the build stage
COPY --from=build /app/target/sireclamations-*.jar sireclamations.jar

# Expose the application port (optional, customize if needed)
EXPOSE 8080

#COPY entrypoint.sh entrypoint.sh

# Run the JAR file
#ENTRYPOINT ["./entrypoint.sh"]

ENTRYPOINT ["java","-jar","/app/sireclamations.jar"]