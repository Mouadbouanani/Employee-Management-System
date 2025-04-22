FROM openjdk:17
COPY target/employee-management-system-0.0.1-SNAPSHOT.jar managmentemployee.jar
ENTRYPOINT ["java", "-jar", "managmentemployee.jar"]