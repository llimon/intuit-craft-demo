FROM openjdk:8-jre-alpine

MAINTAINER Craft Demo <jcraft.demo817@gmail.com>

LABEL maintainer=craft-demo \
      app=tweetfeed-micro-jwt \
      scope=runtime \
      service=gateway

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS

EXPOSE 8090 5701/udp

ADD newrelic.tgz /
VOLUME /newrelic/logs
RUN sed -i 's/My Application/tweetfeed-micro-jwt-gateway/' /newrelic/newrelic.yml

ADD https://raw.githubusercontent.com/Eficode/wait-for/master/wait-for /
RUN chmod 755 /wait-for

# add directly the war
ADD *.war /app.war

ENTRYPOINT ["/wait-for", "mysql:3306", "--timeout=300", "--", "java", "-javaagent:/newrelic/newrelic.jar", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.war"]
