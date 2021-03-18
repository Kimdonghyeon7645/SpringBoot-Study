# 9-4. 실제 배포 자동화 진행

앞의 과정으로 **Travis CI, S3, CodeDeploy 연동**까지 구현됬으니,  
이제 실제 **Jar를 배포하여 실행**까지 해보자.

## 1. deploy.sh 파일 추가

이전에 ```step1``` 환경에서 사용한 ```deploy.sh``` 파일처럼,  
```step2``` 환경에서 실행할 ```deploy.sh``` 파일 생성  

```build.gradle```파일과 같은 경로에 ```scripts``` 디렉토리 생성   
-> 여기에 ```deploy.sh``` 스크립트 생성
```shell script
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
JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)  
echo ">> JAR 파일명 : $JAR_NAME"

echo ">> $JAR_NAME 에 실행권한 추가"
chmod +x $JAR_NAME    # 여기서 Jar 파일은 실행권한이 없는 상태이므로, nohup으로 실행할 수 있는 권한 부여

nohup java -jar \
    -Dspring.config.location=classpath:/application.properties,/home/ec2-user/app/application-oauth.properties,home/ec2-user/app/application-real-db.properties, classpath:/application-real.properties \
    -Dspring.profiles.active=real \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &    # nohup 실행 시 CodeDeploy는 무한 대기하는데, 이 이슈를 해결하기 위해 nohup.out 파일을 표준 입출력으로 사용
# 이렇게 안하면 nohup.out 파일이 생기지 않고, CodeDeploy 로그에 표준 입출력이 출력 + nohup이 끝나기 전까지 CodeDeploy로 끝나지 않으니 꼭 이렇게 할 것!
```

## 2. .travis.yml 파일 수정


