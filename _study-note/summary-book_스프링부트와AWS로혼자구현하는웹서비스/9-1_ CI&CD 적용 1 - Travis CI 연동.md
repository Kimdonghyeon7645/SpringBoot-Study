# 9-1. CI&CD 적용 1 - Travis CI 연동하기

![image](https://user-images.githubusercontent.com/48408417/111033906-ac6fe400-8456-11eb-9672-6e59fef14bf0.png)  

-> CI&CD 적용이 끝난 최종 구조

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

#### 프로젝트 repo에 Travis CI 활성화하기

1. [travis-ci 사이트](https://travis-ci.org/) 접속해서 깃허브 로그인
2. 오른쪽 위 **[계정 아이콘 -> Settings]** 클릭
3. **'Legacy Services Integration'** 에서 CI를 적용할 repo 검색 -> 찾은 repo **오른쪽 상태바(토글)** 활성화
4. 활성화한 저장소 클릭하면, 저장소 빌드 히스토리 페이지로 이동 

이렇게 활성화한 저장소 빌드 히스토리 페이지에서, *'No builds for this repository'* 가 뜨면,  
-> **Travis CI 웹사이트**에서 설정은 끝! (```.travis.yml``` 파일로 상세한 설정 가능)

> ### .yml 파일 확장자?
>
> ```.yml``` 파일 확장자 = YAML(야믈)  
> **YAML : JSON에서 괄호를 제거한 것** (YAML 이념 = 기계에서 파싱하기 쉽게, 사람이 다루기 쉽게)  
> -> 익숙하지 않더라도 읽고 쓰기 쉬움 (+ ```.properties```에 비해 계층형 구조를 보기 쉬움)  
> -> 현재 많은 프로젝트, 서비스들이 YAML 적극적으로 사용(앞으로 쓸 AWS Code-Deploy도 YAML 씀)

## 2. .travis.yml 설정으로 CI 적용

프로젝트의 ```build.gradle```과 같은 경로에 ```.travis.yml``` 생성 후, 아래 코드 추가
```yaml
language: java
jdk:
  - openjdk8
# 프로젝트의 언어, jdk 버전 설정

branches:
  only:
    - master
# 어느 브렌치에 푸시될 때 Travis CI를 작동시킬지 설정
# 현재는 오직 'master' 브렌치에 푸시될 때만 작동

# Travis CI 서버의 Home
cache:
  directories:
    - '$HOME/.m2/repository'
    - '$HOME/.gradle'
# 그레이들로 의존성을 받게되면, 해당 디렉토리에 캐시해서  
# '같은 의존성은 다음 배포 때부터 다시 받지 않도록' 설정

script: "./gradlew clean build"
# 푸시될 때 실행시킬 명령어

# CI 실행 완료시 알림
notifications:
  email:
    recipients:
      - kkddhh2826@gmail.com
# 꼭 메일일 필요 없음 (slack 같은 것도 가능)

# -------- 아래 내용은 책에서 없는 내용 추가 --------
# /home/travis/.travis/functions: line 351: ./gradlew: Permission denied
# ./gradlew 파일 실행 권한이 없는 경우
before_install:
  - chmod +x gradlew
```
이제, master 브렌치에 커밋+푸시 하면, Travis CI 저장소 페이지에서 빌드 성공을 확인!  
(메일로도 빌드 성공을 잘 전달받아 확인!)
