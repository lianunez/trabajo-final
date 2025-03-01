FROM openjdk:17-slim
COPY presentation/target/presentation-0.0.1-SNAPSHOT.jar /app/presentation-0.0.1-SNAPSHOT.jar
EXPOSE 8080
RUN chmod a+x /app/
CMD java -jar /app/presentation-0.0.1-SNAPSHOT.jar