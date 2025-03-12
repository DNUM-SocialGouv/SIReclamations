FROM arm64v8/eclipse-temurin
#USER nonroot
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} sireclamations.jar
ENTRYPOINT ["java","-jar","/sireclamations.jar"]