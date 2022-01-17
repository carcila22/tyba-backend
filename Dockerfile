FROM openjdk:11

RUN mkdir /code
COPY lib /code

ENTRYPOINT [ "sh", "-c", "java -jar -Duser.timezone=$TIMEZONE -Dsun.net.client.defaultConnectTimeout=3000 -Dsun.net.client.defaultReadTimeout=3000 -Xms2048m -Xmx2048m -server -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Dnetworkaddress.cache.ttl=60 -Dnetworkaddress.cache.negative.ttl=10 /code/tyba-backend-0.0.1-SNAPSHOT.jar" ]