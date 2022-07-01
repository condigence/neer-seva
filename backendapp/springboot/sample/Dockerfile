FROM jdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]






# FROM jenkins/jenkins:2.182
#
#
# COPY plugins.txt /plugins.txt
#
# RUN
#
# ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false