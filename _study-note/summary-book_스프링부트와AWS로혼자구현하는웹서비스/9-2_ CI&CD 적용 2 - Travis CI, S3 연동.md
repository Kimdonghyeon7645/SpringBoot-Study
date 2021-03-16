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

## 3. 
