# 基础镜像
FROM eclipse-temurin:17
# 作者
MAINTAINER yshujia
# 工作目录
WORKDIR /home/java
# 设置时区
ENV TZ=Asia/Shanghai
# 同步docker内部的时间
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 端口
EXPOSE 9898
EXPOSE 9092

ENTRYPOINT ["java","-Dspring.config.location=/home/java/application.yml","-jar","/home/java/yshujia.jar"]