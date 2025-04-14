#!/bin/bash

DEPLOY_LOG_PATH=/var/log/deploy/deploy.log

echo $(pwd) >> $DEPLOY_LOG_PATH

JAR_PATH=$(ls /home/ec2-user/app/artisan/artisan-api/build/libs/artisan-api-*.jar | grep -v "plain" | head -n 1)

echo "jar path" >> $DEPLOY_LOG_PATH
echo $JAR_PATH >> $DEPLOY_LOG_PATH

mv $JAR_PATH ./application.jar

CURRENT_PID=$(pgrep -f application.jar)

if [ -z $CURRENT_PID ]
then
  echo ">>> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> $DEPLOY_LOG_PATH
else
  echo ">>> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
  echo ">>> kill application" >> $DEPLOY_LOG_PATH
fi

echo ">>> APP start" >> $DEPLOY_LOG_PATH

nohup java -jar ./application.jar -Dhost.ip=$(hostname -I | awk '{print $1}') --spring.profiles.active=prod --server.port=8080 > /var/local/app.log 2>&1 &