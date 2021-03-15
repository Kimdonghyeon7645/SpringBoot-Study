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

1. 
