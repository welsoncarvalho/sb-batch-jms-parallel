FROM openjdk:8-jre-alpine

ENV ACTIVEMQ_VERSION 5.16.2
ENV ACTIVEMQ_WORKDIR /app

RUN mkdir -p $ACTIVEMQ_WORKDIR

RUN wget http://archive.apache.org/dist/activemq/activemq-artemis/2.17.0/apache-artemis-2.17.0-bin.tar.gz

RUN tar -xzf apache-artemis-2.17.0-bin.tar.gz -C $ACTIVEMQ_WORKDIR
RUN ./$ACTIVEMQ_WORKDIR/apache-artemis-2.17.0/bin/artemis create batch_broker --user u_batch --password p_batch --require-login --queues person_save_queue,person_save_queue_reply,person_convert_queue

EXPOSE 8161
EXPOSE 61616

ENTRYPOINT ["/batch_broker/bin/artemis", "run"]