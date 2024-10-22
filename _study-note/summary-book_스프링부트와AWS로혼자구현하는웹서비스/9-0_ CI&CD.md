# 9. CI, CD

지금까지 **빌드, 배포**는 쉘 스크립트로 그나마 간편히 진행했지만 그럼에도,  

1. *수동 실행하는 Test* : 새로 작성한 코드가 다른 코드에 영향을 끼치는지, 매번 **전체 테스트를 직접 수행**해야 함
2. *수동 Build* : 다른 사람 브렌치와 내 브렌치를 병합(Merge)했을 때 이상이 없는지, 매번 **빌드를 직접 수행**해야 알 수 있음.

과 같은 수동 *Test & Build*의 불편함이 있다.  
특히 여러 개발자가 실시간으로 작업하는 환경이기에, 수동으로 하자면 매번 번거롭고 실수할 여지가 많다.  
-> **깃허브에 푸시를 하면 자동으로 Test & Build & Deploy**가 진행되도록 작업할 필요가 있다.

## 1. CI & CD 란

8장에선 스프링 부트 프로젝트를 EC2에 간단히 배포하면서, 스크립트를 직접 실행함으로 발생하는 불편 겪었음  
-> CI, CD 환경을 구축해 해결 가능   

- **CI(Continuous Integration - 지속적 통합)** 

    : 코드 버전을 관리하는 VCS 시스템(Git, SVN)에서 **PUSH 할때 -> 자동으로 테스트와 빌드가 수행 -> 안정된 배포파일을 만드는 과정** 

- **CD(Continuous Deployment - 지속적인 배포)** 

    : **빌드 결과를 자동으로 운영 서버에 무중단 배포**까지 진행하는 과정  
    (일반적으로 CI 구축만 하기보단, CD와 함께 구축함)


> ### CI/CD가 없었을 적에는...
>
> 현대 웹 서비스 개발 : 한 프로젝트를 여러 개발자가 함께 진행  
> -> 각자 개발한 코드를 합칠때가 큰 일 (아예 매주 **병합일**(코드 Merge만 하는 날)을 진행하기도 함)  
> -> 이런 수작업으로 생산성 하락, 개발자들은 지속해서 코드가 통합되는 환경, **CI**를 구축하게 됨  
> -> 이제 **더는 수동으로 코드를 통합**할 필요 없어짐, 개발자들도 개발에만 집중 가능! 🐱‍💻 
>
> **CD**도 마찬가지,
> -> 1~2대 서버는 수동으로 배포할 수 있지만, 수백대 서버에 배포 or 긴박하게 당장 배포하는 상황 = 수동 배포 불가    
> -> 이 역시 자동화, 개발자들도 개발에만 집중 가능! 🐱‍💻

> ### CI 도구를 쓴다고 CI를 하는 건 아니다?
>
> [마틴 파울러의 블로그](https://johngrib.github.io/wiki/Continuous-Integration/) 참고했을 때, CI엔 4가지 규칙이 있음
>
> 1. 모든 소스 코드가 살아있고(현재 실행되고) + 누구든 **현재 소스에 접근 가능한 단일 지점을 유지**할 것
> 2. 빌드 프로세스 자동화로 -> 누구든 소스로부터 **시스템을 빌드하는 단일 명령어를 사용 가능** 하게할 것
> 3. 테스팅 자동화로 -> 언제든지 **단일 명령어로 시스템의 건전한 _테스트 슈트_ 실행 가능** 하게할 것 
> 4. 누구나 **현재 실행 파일 = 지금까지 가장 완전한 실행 파일** 얻은 확신을 하게할 것
>
> 특히 **테스팅 자동화**가 중요 =지속적으로 통합하기 위해, 무엇보다 프로젝트가 **완전한 상태임을 보장**하기 위해  
> (테스트 코드 작성, TDD에 대해 배우고 싶다면, [백명석님 클린코더스 - TDD편 참고](https://www.youtube.com/channel/UCkdVHy6DWwmEim-xjy81x8A/videos))
