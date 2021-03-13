# 9-1. Travis CI, CodeDeploy, S3로 CI/CD 적용하기

![image](https://user-images.githubusercontent.com/48408417/111033906-ac6fe400-8456-11eb-9672-6e59fef14bf0.png)  

배포 자동화 전체 구조

## 1. Travis(트레비스) CI

: 깃허브에서 제공하는 CI 서비스 (오픈소스는 무료, 비공개는 유료)

> ### Travis CI를 고르는 이유?
>
> CI 툴은 *Travis CI* 말고도 *젠킨스, AWS의 CodeBuild* 등이 있다.  
> 그럼에도 책에서 *Travis CI*를 선택하는 이유는,
> 
> - 젠킨스 : 설치형이라, 이를 위한 EC2 인스턴스가 하나 더 필요 -> 초기에 사용시 부담
> - CodeBuild : 빌드 시간만큼 요금이 부과 -> 초기에 사용시 부담
> 
> 따라서 비용 부담이 가장 적은 *Travis CI*를 사용한다. (초기 서비스 + 공개 repo 기준)  
> (비공개 repo에선, *AWS CodeBuild*를 추천 [(참고)](https://velog.io/@city7310/%EB%B0%B1%EC%97%94%EB%93%9C%EA%B0%80-%EC%9D%B4%EC%A0%95%EB%8F%84%EB%8A%94-%ED%95%B4%EC%A4%98%EC%95%BC-%ED%95%A8-11.-%EB%B0%B0%ED%8F%AC-%EC%9E%90%EB%8F%99%ED%99%94))


