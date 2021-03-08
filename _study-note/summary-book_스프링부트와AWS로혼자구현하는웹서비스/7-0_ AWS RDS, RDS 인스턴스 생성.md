# 7. AWS RDS

> #### 백엔드가 왜 DB를?
>
> 웹 서비스 백엔드에서, **어플리케이션 코드 작성만큼 중요한 것이 DB 다루는 일**  
> 물론, 큰규모 회사엔 대용량/대량의 데이터를 다루기에 DB만 담당하는 DBA라는 담당자가 있지만, 그러한 이유는 전문성이 필요한 것 때문이다.  
> 스타트업/소규모 서비스에선 **개발자가 DB를 알고, 다룰수 있어야 함**

### 1. RDS?

직접 DB를 설치하면, *'모니터링, 알람, 백업, HA 구성등'* 을 모두 직접 해야된다. (처음 구축시 며칠 걸릴 수 있음)  
-> AWS에선 위의 작업들을 모두 지원하는 **관리형 서비스, RDS(Relational Database Service)** 를 제공

RDS의 특징은,
- **클라우드 기반의 관계형 DB(=RDB)** 
- 운영 작업(하드웨어 프로비저닝, DB 설정, 패치 및 백업)을 자동화 -> 개발자는 개발에 집중 가능
- **조정 가능한 용량** 지원 -> 예상 외 데이터로 용량이 넘쳐도, 비용만 내면 정상 이용 가능

### 2. RDS 인스턴스 생성

1. 검색창에 rds 검색
2. rds 대시보드 > **[데이터베이스 생성]** 클릭

하면 RDS 생성 과정이 진행된다. 차례대로 선택하며 진행하자.

1. 데이터베이스 생성 방식 선택

    **표준 생성** 그대로 선택  
    (손쉬운 생성은 DB엔진만 골라 인스턴스 생성 뒤, 추가로 설정을 변경 가능)
    
2. 엔진 옵션

    본인이 쓰는거 아니면, **MariaDB** 선택
    
    > #### DB 엔진, MariaDB를 추천하는 이유
    >
    > RDS에는 *'오라클, MSSQL, PostgreSQL, MySQL, MariaDB, Amazon 오로라'* 등이 있다.
      특정 DB를 쓸 이유가 없다면, **MySQL, MariaDB, PostgreSQL** 를 추천하고, 그중에 **MariaDB** 추천
    >
    > **MariaDB** 쓰는 이유
    > 1. 가격 : RDS 가격은 라이센스 비용 영향 받음 
    >
    >   오픈소스인 MySQl, MariaDB, PostgreSQL가 오라클, MSSQL보다 같은 사양대비 가격 높음
    > 
    > 2. Amazon Aurora(오로라) 교체 용이성
    >
    >   AWS 오로라 : AWS에서 **MySQL, PostgreSQL을 클라우드 기반에 맞게 재구성한 DB**   
       -> RDS MySQL 대비 5배, RDS PostgreSQL 대비 3배 성능 제공 + AWS에서 직접 엔지니어링  
       (근데 AWS 오로라를 직접 쓰기엔, 프리티어 대상 아니며, 최소 월 10만원 이상이라... 일단 MariaDB로 하다 나중에 오로라로 이전)
    
    > #### MariaDB란?
    > 
    > 국내외 가장 인기있는 오픈소스 DB = MySQL (단순 쿼리 처리 성능이 압도적, 오래 사용되어 성능/신뢰성 꾸준히 개선)  
      MySQL이 합병되면서 개발자들이 떠나와 본인만의 프로젝트를 진행하게 됨  
      -> MySQL 창시자가 만든 프로젝트 = **MariaDB (MySQL 기반)**
    > 
    > - MySQL과 쿼리같은 전반적인 사용법 유사
    > - 동일 HW 사양에서 MySQL보다 향상된 성능
    > - 좀 더 활성화된 커뮤니티
    > - 다양한 기능, 스토리지 엔진


 
- 템플릿에서 프리티어를 선택한다.

- 스토리지는 20GIB로 설정해 준다.

- 마스터 사용자 이름과 마스터 암호를 설정해 주고, 식별자(이름)을 지정해 준다.

- 그리고 퍼블릭 엑세스 기능을 예 로 설정해야 외부에서 접근할 수 있다.

### 파라미터 설정

- 우선 파라미터 왼쪽의 파라미터 그룹 탭으로 이동한다.
- 기존의 파라미터를 선택하는게 아니라, 새로 생성해 준다.
- DB 엔진을 선택해야 하는데, 방금 생성한 DB와 버전을 맞춰줘야 한다.
- 생성된 파라미터 그룹을 선택해 준다.
- 오른쪽 위 편집 버튼을 누른다.
- 그리고 time_zone을 검색해서 Asia/Seoul로 변경해 준다.
- 그리고 character_set을 검색해서 모두 utf8mb4로 변경해 준다.
  - character_set_client
  - character_set_connection
  - character_set_database
  - character_set_filesystem
  - character_set_results
  - character_set_server
- 그리고 collation을 검색해서 utf8mb4_general_ci로 변경해 준다.
  - collation_connection
  - collation_server
- 마지막으로 max_connections를 검색해서, 조금 더 넉넉하게 지정해 준다.
- 그리고 데이터베이스 탭으로 이동한 후에 DB를 선택하고, 수정을 눌러준 다음 파라미터 그룹을 바꿔준다.
- 아직 서비스를 오픈하지 않았으니 즉시 적용을 눌러준다.

### 보안그룹 설정

- 우선 EC2의 보안그룹 id를 복사해서 그 ID를 RDS 보안그룹의 인바운드로 추가한다.
- 그리고 자신의 IP도 추가해 준다.

### 데이터베이스 GUI 클라이언트

- 대표적으로 Mysql Workbench, SQLyog 등이 있지만, 여기서는 IntelliJ의 플러그인(Database Navigator)을 설치해서 한다.
- RDS로 가서 엔드포인트(접근하기 위한 URL 같은거)
- ctrl+shift+a 를 통해 검색을 연다.
- Database Browser 를 검색해서 왼쪽 사이드바에 DB Browser를 띄워준다.
- \+버튼을 눌러 MySQL을 선택한다(MariaDB 또한 MySQL을 선택한다)
- Host에는 엔드포인트, 포트에는 3306, user에는 아이디, password에는 비밀번호를 입력하고 Test Connection을 눌러 연결 테스트를 한다.
- 성공했다면 Apply, OK 버튼을 눌러 저장을 한다.
- Open SQL Console을 눌러주고, New SQL Console을 눌러서 콘솔창을 만들어 준다.
- 콘솔창에서 useDB명 을 치고, 드래그 한 후에 Execute Statement 클릭해 준다.

### EC2에서 RDS 접근 확인하기

- sudo yum install mysql을 통해 mysql을 설치한다.
- 설치했다면 mysql -u 아이디 -p -h 엔드포인트 를 친다.
- 그 이후에 비밀번호를 입력하라고 나오는데, 비밀번호를 입력한다.
