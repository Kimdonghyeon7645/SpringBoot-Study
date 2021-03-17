# 9-2. CI&CD 적용 2 - Travis CI, S3 연동하기

![image](https://user-images.githubusercontent.com/48408417/111033906-ac6fe400-8456-11eb-9672-6e59fef14bf0.png)  

-> CI&CD 적용이 끝난 최종 구조

## 1. EC2에 CodeDeploy 연동 권한(IAM 역할) 추가 

CodeDeploy를 사용하기 전, 배포 대상인 **EC2가 CodeDeploy와 연동되도록 'IAM 역할'** 생성

> ### IAM 역할과 사용자의 차이점
>
> - 역할 : AWS 서비스(EC2, CodeDeploy, SQS 등)에만 할당 가능한 권한
> - 사용자 : **AWS 서비스 외**(로컬PC, IDC 서버 등)에만 사용 가능한 권한

#### IAM 역할 만들기

1. 전처럼 AWS 웹 콘솔에서 *IAM* 검색 -> **IAM 페이지**로 이동
2. IAM 페이지 왼쪽 사이드바 **[역할]** 클릭 -> **[역할 만들기]** 클릭
3. '신뢰할 수 있는 유형의 개체 선택' : **AWS 서비스** 선택
4. '사용 사례 선택' : **EC2** 선택 후, **[다음: 권한]** 클릭
5. 권한 : **AmazonEC2RoleforAWSCodeDeploy** 검색 후 선택 -> **[다음: 태그]** 클릭
6. 태그는 키:Name, 값:원하는 이름으로 입력 후, **[다음: 검토]** 클릭
7. 역할 이름 입력하고 입력한 값 검토 후, **[역할 만들기]** 클릭

#### EC2 서비스에 역할 등록하기

1. EC2 인스턴스 목록으로 이동
2. 본인 인스턴스 오른쪽 클릭 -> **[보안 > IAM 역할 수정]** 선택
3. IAM 역할에 아까 생성한 역할 선택 후, **[저장]** 클릭

역할 선택 후, 해당 **EC2 인스턴스를 재부팅** (그래야 역할이 정상적으로 적용)  


## 2. CodeDeploy 에이전트 설치

CodeDeploy의 요청을 받을 수 있게 에이전트 설치    
(에이전트 : ec2 배포환경에만 설치하는 프로그램, CodeDeploy에서 해당 ec2를 사용할 수 있게 해줌 [(참고)](https://galid1.tistory.com/745))

1. EC2 접속해서 아래 명령어 입력
    ```shell script
    aws s3 cp s3://aws-codedeploy-ap-northeast-2/latest/install . --region ap-northeast-2
    ```
    콘솔에 ```download: s3://aws-codedeploy-ap-northeast-2/latest/install to ./install```가 출력되면 다운로드 성공
2. install 파일로 설치 진행
    ```shell script
    chmod +x ./install   # install 파일에 우선 실행 권한을 추가
    
    sudo ./install auto    # install 파일 실행해서 설치 진행
    # 설치 중에,  /usr/bin/env: ruby: No such file or directory
    # 에러 발생시 루비라는 언어가 설치 안되서 그러니까
    # sudo yum install ruby
    # 로 루비 설치 (그 후 다시 install 파일 실행)
    ```

설치가 끝났으면,  
```shell script
sudo service codedeploy-agent status
```
로 상태 검사 했을 때, ```The AWS CodeDeploy agent is running as PID (pid값)``` 같이 running 메시지가 출력되면 정상!


## 3. CodeDeploy의 EC2 접근 권한(IAM 역할) 생성 

EC2에서 CodeDeploy에 연동하기 위한 IAM 역할을 생성했듯이 -> **CodeDeploy에서 EC2에 접근하기 위한 IAM 역할 생성**  

1. 아까 IAM 역할 만들때 처럼, **IAM 페이지**에서 **[역할]** -> **[역할 만들기]** 클릭
2. '신뢰할 수 있는 유형의 개체 선택' : **AWS 서비스** 선택
3. '사용 사례 선택' : **CodeDeploy** 선택 후, 밑의 CodeDeploy 종류에서 걍 **CodeDeploy** 선택
4. **[다음: 권한]** 클릭 후, (차피 CodeDeploy는 권한이 하나뿐이라) 권한 선택은 패스하고 **[다음: 태그]** 클릭
5. 키는 Name, 값은 원하는 이름으로 짓고, **[다음: 검토]** 클릭 -> 역할 이름 입력하고 입력값 검토 후, **[역할 만들기]** 클릭


## 4. CodeDeploy 생성

CodeDeploy는 *AWS의 배포 삼형제* 중 하나로, CD

> ### AWS 배포 삼형제
> 
> 1. Code Commit 
>   - 깃허브 같은 코드 저장소 역할
>   - private 기능을 지원하는 강점이 있지만, 이미 **깃허브에서 무료로 private를 지원**하기에 안 쓰임
> 2. Code Build
>   - Travis CI와 같은 **빌드용 서비스**
>   - [멀티 모듈](https://woowabros.github.io/study/2019/07/01/multi-module.html) 배포시 매력적
>   - 하지만, 규모있는 서비스는 대부분 **젠킨스/팀시티 등을** 사용하기에 안 쓰임
> 3. CodeDeploy
>   - AWS 배포 서비스
>   - CodeDeploy는 대체제가 없음
>   - 오토 스케일링 그룹 배포, 블루 그린 배포, 롤링 배포, EC2 단독 배포 : 많은 기능 지원
> 
> 현재 진행 중인 프로젝트에선 Code Commit 역할을 *깃허브*가, Code Build 역할을 *Travis CI*가 하고 있음  

1. AWS 웹 콘솔에서 **CodeDeploy** 검색 -> **CodeDeploy 페이지**로 이동
2. 왼쪽 사이드바 상단의 제목, **CodeDeploy** 클릭 -> **[애플리케이션 생성]** 클릭
3. 원하는 CodeDeploy 이름 입력, 컴퓨팅 플랫폼: **EC2/온프레미스** 선택 후, **[애플리케이션 생성]** 클릭 
4. 생성 완료 후 페이지에서 **[배포 그룹 생성]** 클릭
5. 원하는 배포 그룹 이름 입력, 서비스 역할: 전에 생성한 **CodeDeploy용 IAM 역할** 선택
6. 배포 유형: **현재 위치** 선택 (배포할 서비스의 EC2가 두대 이상 -> *블루/그린* 선택, 현재 프로젝트 같은 1대 EC2라면 *현재 위치* 선택)
7. 환경 구성에서 **[Amazon EC2 인스턴스]** 체크
8. 태그 그룹에서 키: Name, 값: 검색폼에서 자동완성 뜨는 값으로 입력
9. 배포 구성은 ```CodeDeployDefault.AllAtOnce``` 선택, 로드 밸런싱은 체크 해제

> ### 배포 구성, CodeDeployDefault.AllAtOnce 의미
>
> - 배포 구성 : 한번에 배포할 때 몇 대의 서버에 배포할 지 선택    
>   -> 2대 이상 서버면 1대씩 or 30% or 50%로 나눠 배포 등의 옵션을 선택  
>   -> 현재 프로젝트는 1대 서버다 보니 전체 배포하는 옵션(= *CodeDeployDefault.AllAtOnce* = 한번에 다 배포) 선택  


## 5. Travis, S3, CodeDeploy 연동

먼저 S3에서 넘겨줄 **zip 파일 저장할 디렉토리**(```~/app/step2/zip```) 생성  
-> EC2 서버에서 아래 명령어 실행
```shell script
mkdir ~/app/step2 && mkdir ~/app/step2/zip

# Travis CI의 빌드 끝나면 S3에 zip 파일 전송 
# home/ec2-user/app/step2/zip 로 복사되어 압축 풀 예정 
```

AWS CodeDeploy 설정을 하기 위해, ```.travis.yml```과 같은 위치에 ```appspec.yml``` 생성  
-> ```appspec.yml```에 아래 코드 입력
```yaml
version: 0.0
os: linux
files:
    - source: /
      destination: /home/ec2-user/app/step2/zip/
      overwrite: yes
```

```.travis.yml```에도 CodeDeploy 내용 추가
```yaml
deploy:
    ...

    - provider: codedeploy
      access_key_id: $AWS_ACCESS_KEY    # Travis repo settings 에서 설정한 값
      secret_access_key: $AWS_SECRET_KEY    # Travis repo settings 에서 설정한 값
      bucket: 버킷명   # S3 버킷명
      key: springboot-webservice.zip      # 빌드 파일을 압축해 전달
      bundle_type: zip    # 압축 확장자
      application: 웹콘솔에서 등록한 애플리케이션명
      deployment_group: 웹콘솔에서 등록한 CodeDeploy 배포그룹
      region: ap-northeast-2
      wait-until-deployed: true
```