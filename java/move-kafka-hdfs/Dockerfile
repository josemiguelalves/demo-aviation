FROM bde2020/spark-submit:2.3.0-hadoop2.7

MAINTAINER Jose Miguel <jose.miguel.usp@gmail.com>

ARG JAR_NAME=traffic-data-orc-1.0-SNAPSHOT

ENV SPARK_APPLICATION_JAR_NAME $JAR_NAME
ENV SPARK_APPLICATION_MAIN_CLASS com.iot_datamining.dataingestion.TrafficDataOrc
ENV SPARK_SUBMIT_ARGS --executor-memory 4G --total-executor-cores 4


COPY template.sh /


RUN  chmod +x /template.sh \
       && mkdir -p /app \
       && mkdir -p /usr/src/app/target


# Add the application's jar to the container
ADD ./target/$JAR_NAME.jar /usr/src/app/target/$JAR_NAME.jar

CMD ["/bin/bash", "/template.sh"]