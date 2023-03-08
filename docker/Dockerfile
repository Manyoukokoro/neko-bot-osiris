FROM openjdk:17
COPY config.yaml /usr/local/bot/
COPY neko-bot-osiris-1.0.0-jar-with-dependencies.jar /usr/local
WORKDIR /usr/local/
ENV TIME_ZONE Asia/Shanghai
ENTRYPOINT ["java","-jar","/usr/local/neko-bot-osiris-1.0.0-jar-with-dependencies.jar"]