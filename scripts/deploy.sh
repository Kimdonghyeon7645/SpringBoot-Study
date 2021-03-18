#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2   # 이젠 위치가 /step2 이다
PROJECT_NAME=SpringBoot-Study

# /step1에선 git pull 받아서 직접 빌드 해줬지만, /step2에선 이미 빌드되어있는 파일을 받아오기에 생략

echo ">> Build 파일 복사"
cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo ">> 현재 구동 중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -fl ${PROJECT_NAME} | grep jar | awk '{print $1}')   # pgrep = process id만 추출하는 명령어(-f = 프로세스 이름으로 찾기)
# /step1처럼 현재 수행중인 스프링부트 애플리케이션의 pid(프로세스 id)를 찾아 실행중이면 종료하는 것은 똑같은데,
# 스프링부트 어플리케이션 이름으로 된 다른 프로그램이 있을 수 있으니,
# 어플리케이션 이름으로 된 jar 프로세스를 찾은 뒤, (pgrep -fl ${PROJECT_NAME} | grep jar)
# ID를 찾음 (awk '{print $1}')

echo "현재 구동 중인 애플리케이션 pid : $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo ">> 현재 구동 중인 애플리케이션이 없으므로 종료 X"
else
    echo ">> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo ">> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)    # /step2에서 수정
echo ">> JAR 파일명 : $JAR_NAME"

echo ">> $JAR_NAME 에 실행권한 추가"
echo "^^ $(ll)}"
chmod +x $JAR_NAME    # 여기서 Jar 파일은 실행권한이 없는 상태이므로, nohup으로 실행할 수 있는 권한 부여

nohup java -jar \
    -Dspring.config.location=classpath:/application.properties,/home/ec2-user/app/application-oauth.properties,home/ec2-user/app/application-real-db.properties, classpath:/application-real.properties \
    -Dspring.profiles.active=real \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &    # nohup 실행 시 CodeDeploy는 무한 대기하는데, 이 이슈를 해결하기 위해 nohup.out 파일을 표준 입출력으로 사용
