# 9-2. CI&CD 적용 2 - Travis CI, S3 연동하기

![image](https://user-images.githubusercontent.com/48408417/111033906-ac6fe400-8456-11eb-9672-6e59fef14bf0.png)  

-> CI&CD 적용이 끝난 최종 구조

AWS S3라는 **파일 서버**를 추가로 연동하는 이유 = **Jar 파일을 전달하기 위해 (CodeDeploy는 저장 기능 없음)**  

> ### 사실 CodeDeploy로 빌드, 배포를 할 수 있다?
>
> 사실 CodeDeploy로 빌드도 배포도 할 수 있다. (CodeDeploy에서 깃허브 코드를 가져오는 기능을 지원하기 때문)    
> 하지만 그경우, **빌드없이 배포만 할 때 대응하기 어렵다.**  
> -> 빌드와 배포가 분리됬다면, 배포만 할 땐 예전에 만든 Jar를 재사용할 수 있지만, CodeDeploy에선 항상 빌드  
> -> 확장성이 많이 떨어지니 가능한 빌드와 배포는 분리하는 것이 좋음

## 1. IAM (AWS Key) 발급 
 
**Travis CI**와 **AWS S3**을 연동하기 위해, **접근가능 권한을 가진 Key**를 만들어 사용해야 함 (일반적으로 AWS 서비스에 **외부 서비스가 접근 불가**)

AWS는 이러한 서비스로 **IAM(Identity and Access Management)** 지원  
-> IAM은 AWS 서비스의 접근방식과 권한을 관리 (=인증 관련 기능 제공)  
-> IAM으로 Travis CI가 AWS가 제공하는 S3, CodeDeploy에 접근 가능

#### IAM 생성하기

1. AWS 웹 콘솔에서 *IAM* 검색 -> **IAM 페이지**로 이동
2. IAM 페이지 왼쪽 사이드바 **[사용자]** 클릭 -> **[사용자 추가]** 클릭
3. 생성할 **사용자 이름, 엑세스 유형**(-> 유형은 **프로그래밍 방식 엑세스**로) 선택 후, 다음 단계로 
4. 권한 설정은 3개 중 **기존 정책 직접 연결**을 선택
5. 밑에 생기는 정책 검색 화면에서 연결할 정책 체크 후, 다음 단계로 
    - **s3full** 검색 후 (AmazonS3FullAccess 항목에) 체크 
    - **codedeployfull** 검색 후 (AWSCodeDeployFullAccess 항목에) 체크
    - (실제 서비스에선 S3와 CodeDeploy 권한도 분리해 관리하기도 하지만, 여기선 둘을 합쳐서 간단히 관리)
6. 태그에서 **키: Name, 값: 본인이 인지 가능한 이름**으로 생성 후, 다음 단계로
7. 설정했던 값을 다시 확인 후 **[사용자 만들기]** 클릭 

최종 생성이 끝나면, **엑세스 키, 비밀 엑세스 키**가 생성! -> **Travis CI에서 사용될 키**  
(비밀 엑세스 키는 한번만 보거나 다운할 수 있다. 놓쳤으면 새 엑세스 키를 생성하자)

## 2. Travis CI에 키 등록

1. 먼저 Travis CI 사이트의 설정 화면으로 이동
2. 활성화된 repo의 Settings 페이지의 **Environment Variables** 항목 찾기
3. IAM 사용자에서 발급받은 키 값들 등록
    - NAME: *AWS_ACCESS_KEY*, VALUE: *엑세스 키 ID*
    - NAME: *AWS_SECRET_KEY*, VALUE: *비밀 엑세스 키*
    
여기서 등록된 값들은 ```.travis.yml```에서 ```$AWS_ACCESS_KEY```, ```$AWS_SECRET_KEY```로 불러와 사용 가능!  

## 3. S3 버킷 생성

> ### AWS의 S3 서비스
>
> S3(Simple Storage Service)는 일종의 **파일 서버**  
> -> 순수하게 파일들을 저장하고 접근권한에 따라 관리, 검색 등을 지원하는 파일 서버의 역할
> 
> 보통 게시글을 쓸 때 나오는 첨부파일 등록을 구현할 때 (이미지 저장시에) 많이 이용  
> *여기서는 Travis CI에서 생성된 **Build 파일(Jar)을 저장**하도록 구성* (이후 CodeDeploy에서 배포할 파일로 가져가도록 구성 예정) 

1. AWS 웹 콘솔에서 S3 검색 -> S3 페이지로 이동
2. **[버킷 만들기]** 클릭 
3. 원하는 버킷 이름 작성 (**배포할 Zip 파일이 모여있을 장소**임)
4. 모든 퍼블릭 엑세스 차단을 체크했는지 확인
    - 실제 서비스에선 Jar 파일이 퍼블릭이면 누구나 다운받아 중요정보 탈취될 수 있음  
    - 퍼블릭이 아니여도 IAM 사용자로 발급받은 키를 사용하기에 접근 가능 -> 퍼블릭 엑세스 모두 차단

## 4. .travis.yml에 코드 추가

이제 Travis CI에 빌드한 Jar 파일을 S3에 올릴 수 있도록, ```.travis.yml```파일에 아래 코드를 추가
```yaml
...

before_deploy:    # deploy 명령어 실행되기 전 수행
    - zip -r 압축파일이름 *   # 현재 위치 모든 파일(*)을 압축파일이름으로 압축(zip)
    - mkdir -p deploy   # deploy 라는 디렉토리를 Travis CI가 실행중인 위치에 생성
    - mv 압축파일이름.zip deploy/압축파일이름.zip   # 압축한 파일을 deploy 디렉토리로 이동
# 'CodeDeploy가 Jar 파일을 인식하지 못하므로' -> Jar와 기타 설정 파일을 모두 모아 압축(zip)

deploy:   # S3 파일 업로드 or CodeDeploy로 배포 등 = 외부 서비스와 연동될 행위들 선언
    - probider: s3
      access_key_id: $AWS_ACCESS_KEY    # Travis repo settings 에서 설정한 값
      secret_access_key: $AWS_SECRET_KEY    # Travis repo settings 에서 설정한 값
      bucket: 버킷명   # S3 버킷명
      region: ap-northeast-2
      skip_cleanup: true    # 빌드가 끝난 뒤 결과물을 삭제(cleanup)하지 않겠다는 의미
      acl: private    # zip 파일 접근을 private
      local_dir: deploy   # 아까 생성한 deploy 디렉토리 지정 -> '해당 위치의 파일'들만 S3로 전송
      wait-until-deployed: true
# 들여쓰기 잘못하지 않도록 주의!

...
```

설정 끝났으면 **깃허브로 푸시**  
-> Travis CI에서 자동으로 빌드 진행 확인! -> 모든 빌드 성공 확인!

마지막으로 S3 버킷을 가봤을때 업로드가 성공했는지 확인!
