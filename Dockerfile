## build container
FROM openjdk:8 AS TEMP_BUILD_IMAGE

ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

COPY build.gradle settings.gradle gradle.properties gradlew $APP_HOME
COPY gradle $APP_HOME/gradle
COPY src $APP_HOME/src

RUN chmod +x ./gradlew
RUN ./gradlew build -x test || return 0

## actual container
FROM openjdk:8
LABEL author="Staffan Näse <ext-staffan.nase@opuscapita.com>"

## setting heap size automatically to the container memory limits
ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -XshowSettings:vm"

ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/as-gateway.jar .

HEALTHCHECK --interval=15s --timeout=30s --start-period=40s --retries=15 \
  CMD curl --silent --fail http://localhost:3037/api/health/check || exit 1

EXPOSE 3056
ENTRYPOINT exec java $JAVA_OPTS -jar as-gateway.jar