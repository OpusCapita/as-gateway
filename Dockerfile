########## BUILD ##########
FROM openjdk:8 AS TEMP_BUILD_IMAGE

ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

COPY mvnw package.json package-lock.json pom.xml webpack.config.js $APP_HOME
COPY .mvn $APP_HOME/.mvn
COPY src $APP_HOME/src
COPY config $APP_HOME/config

RUN chmod +x ./mvnw
RUN ./mvnw clean install || return 0





########## RUN ##########
FROM openjdk:8
LABEL author="Staffan Näse <ext-staffan.nase@opuscapita.com>"

## setting heap size automatically to the container memory limits
ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -XshowSettings:vm"
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

COPY --from=TEMP_BUILD_IMAGE $APP_HOME/target/as-gateway.jar .
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/config .

HEALTHCHECK --interval=15s --timeout=30s --start-period=40s --retries=15 \
  CMD curl --silent --fail http://localhost:3037/api/health/check || exit 1
EXPOSE 3056
ENTRYPOINT exec java $JAVA_OPTS -jar as-gateway.jar