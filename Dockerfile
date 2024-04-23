FROM openjdk:21

COPY out/artifacts/JAVA_ALGOS_jar/JAVA_ALGOS.jar /JAVA_ALGOS.jar

CMD ["java", "-jar", "/JAVA_ALGOS.jar"]