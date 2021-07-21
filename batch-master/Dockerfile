FROM openjdk:11

ARG DATASOURCE_URL
ARG DATASOURCE_USER
ARG DATASOURCE_PASS

ARG ARTEMIS_HOST
ARG ARTEMIS_PORT
ARG ARTEMIS_USER
ARG ARTEMIS_PASS

ENV datasource_url=$DATASOURCE_URL
ENV datasource_user=$DATASOURCE_USER
ENV datasource_pass=$DATASOURCE_PASS

ENV artemis_host=$ARTEMIS_HOST
ENV artemis_port=$ARTEMIS_PORT
ENV artemis_user=$ARTEMIS_USER
ENV artemis_pass=$ARTEMIS_PASS

COPY target/batch-master*.jar /opt/batch-master.jar
COPY wait-for-it.sh /opt/wait-for-it.sh

WORKDIR /opt
RUN chmod +x wait-for-it.sh

ENTRYPOINT ["./wait-for-it.sh", "mysql:3306", "-t", "180", "--", "java", "-jar", "batch-master.jar"]