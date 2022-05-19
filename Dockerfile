FROM openjdk:18.0.1
ENV ARTIFACT_NAME=moneybot.jar
COPY ./target/$ARTIFACT_NAME .
COPY .entrypoint.sh /tmp/
RUN tr -d '\r' < /tmp/.entrypoint.sh > /entrypoint.sh && chmod 0755 /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]