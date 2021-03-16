# 9-2. CI&CD 적용 2 - Travis CI, S3 연동하기

![image](https://user-images.githubusercontent.com/48408417/111033906-ac6fe400-8456-11eb-9672-6e59fef14bf0.png)  

-> CI&CD 적용이 끝난 최종 구조

## 1. EC2에 IAM 역할 추가

CodeDeploy를 사용하기 전, 배포 대상인 **EC2가 CodeDeploy와 연동되도록 'IAM 역할'** 생성

> ### IAM 역할과 사용자의 차이점
>
> - 역할 : AWS 서비스(EC2, CodeDeploy, SQS 등)에만 할당 가능한 권한
> - 사용자 : **AWS 서비스 외**(로컬PC, IDC 서버 등)에만 사용 가능한 권한

#### IAM 역할 만들기

1. 전처럼 AWS 웹 콘솔에서 *IAM* 검색 -> **IAM 페이지**로 이동
2. IAM 페이지 왼쪽 사이드바 **[역할]** 클릭 -> **[역할 만들기]** 클릭
3. 신뢰할 수 있는 유형의 개체 선택 : **AWS 서비스** 선택
4. 사용 사례 선택 : **EC2** 선택 후, **[다음:권한]** 클릭
5. 정책 : **AmazonEC2RoleforAWSCodeDeploy** 검색 후 선택 -> **[다음:태그]** 클릭
6. 태그는 키:Name, 값:원하는 이름으로 입력 후, **[다음:검토]** 클릭
7. 역할 이름 입력 후, 입력한 값 검토가 끝났으면, **[역할 만들기]** 클릭

#### EC2 서비스에 역할 등록하기

1. EC2 인스턴스 목록으로 이동
2. 본인 인스턴스 오른쪽 클릭 -> **[보안 > IAM 역할 수정]** 선택
3. IAM 역할에 아까 생성한 역할 선택 후, **[저장]** 클릭

역할 선택 후, 해당 **EC2 인스턴스를 재부팅** (그래야 역할이 정상적으로 적용)  


## 2. CodeDeploy 에이전트 설치

CodeDeploy의 요청을 받을 수 있게 에이전트 설치

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


