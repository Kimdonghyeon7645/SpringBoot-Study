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
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)    # /step2에서 수정 
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

지금까지는 프로젝트의 모든 파일을 zip 파일로 만들었는데, 실제 필요한 파일들은 **Jar, appspec.yml, 배포를 위한 스크립트들**밖에 없다. (```.travis.yml```은 Travis CI에서만 필요, CodeDeploy에선 불필요)  
-> 이외 나머지는 포함하지 않고 압축하도록 ```.travis.yml``` 파일의 ```before_deploy```(s3에 올릴 압축파일을 만드는 과정)을 수정해준다.    
```yaml
before_deploy:
  - mkdir -p before_deploy  # Travis CI는 S3로 특정 파일만 업로드 불가, 디렉토리 단위로만 업로드 가능 (zip에 포함시킬 파일들을 담을 디렉토리는 항상 생성)
  - cp scripts/*.sh before-deploy/    # 배포를 위한 스크립트들
  - cp appspec.yml before-deploy/    # appspec.yml
  - cp build/libs/*.jar before-deploy/     # Jar
  - cd before-deploy && zip -r before-deploy *  # before-deploy로 이동 후 전체 압축
  
  - cd ../ && mkdir -p deploy   # 상위 디렉토리 경로에서 deploy 디렉토리 생성
  - mv before-deploy/before-deploy.zip deploy/springboot-study.zip    # deploy/로 zip 파일 이동(+이름 변경)
```


## 3. appspec.yml 파일 수정

아래 코드를 ```appspec.yml```에 추가 (들여쓰기 주의! 들여쓰기 잘못되면 배포 실패)
```yaml
permissions:    # CodeDeploy에서 EC2 서버로 넘겨준 파일들을 모두 ec2-user 권한을 갖도록 함
    - object: /
      pattern: "**"
      owner: ec2-user
      group: ec2-user

hooks:      # CodeDeploy 배포 단계에서 실행할 명령어 지정
    ApplicationStart:     # ApplicationStart단계에서 실행 시작
    - location: deploy.sh     # 실행할 명령어(스크립트)
      timeout: 60     # 스크립트 실행 시간 제한(무한정 기다릴 수 없으니, 60초 이상 스크립트가 수행되면 실패)
      runas: ec2-user   # 권한
```

## 4. 실제 배포 자동화 진행해보기

이제 커밋하고 푸시했을 때, **Travis CI, CodeDeploy** 두 곳에서 모두 성공 메시지를 확인하면 배포 성공!

앞으로는 코드 수정시, ```build.gradle``` 에서
```shell script
# version '1.0-SNAPSHOT' 이 부분을
version '1.0.1-SNAPSHOT'    # 이렇게 수정
```
프로젝트 버전을 변경해주고, 실제 눈으로도 변경됬는지 알 수 있게,  
-> ```src/main/resource/templates/index.mustache``` 내용을 아래처럼 살짝 수정해주자.
```html
...
<h1>스프링 부트로 시작하는 웹 서비스 ver.2</h1>
...
```

이후 깃허브로 커밋과 푸시를 했을 때, 브라우저에서 ec2서버 dns 주소로 접속하면, **변경된 코드가 자동으로 배포**된 것을 확인할 수 있다!
