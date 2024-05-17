FROM harbor-test.7moor.com:8443/base/openjdk8-openj9:alpine

COPY target/*.jar /app/abs-api.jar

ENV NACOS_DISCOVERY_ADDR=http://172.31.13.6:32257 \
    NACOS_CONFIG_ADDR=http://172.31.13.6:32257 \
    NACOS_CONFIG_GROUP=beta \
    NACOS_PROFILES_ACTIVE=beta \
    NACOS_DISCOVERY_NAME_SPACE=cba40065-318e-4e7a-ace7-896c88c9a13b \
    NACOS_DISCOVERY_WEIGHT=1

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories && apk add tzdata && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apk del tzdata

#RUN /bin/sh -c 'java -Xshareclasses -Xquickstart -jar /app/abs-api.jar &' ; sleep 60 ; ps aux | grep java | grep abs-api | awk '{print $1}' | xargs kill -1

CMD ["sh", "-c", "java ${JAVA_OPTIONS} -jar /app/abs-api.jar"]