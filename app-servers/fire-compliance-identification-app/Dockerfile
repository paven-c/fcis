#采用openjdk:17-jdk-alpine作为基础镜像
FROM openjdk:17-jdk

#时区配置
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

#自定义一个工作目录:/data
WORKDIR /data

EXPOSE 10091
#拷贝jar到应用目录下,app为自定义的目录名称
COPY target/fire-compliance-identification-app.jar ./app/

#在data目录下启动 ./app/xxx-0.0.1-SNAPSHOT.jar
CMD java -jar ./app/fire-compliance-identification-app.jar