# 7-1. RDS 운영환경에 맞게 파라미터 설정

RDS 처음 생성후, 필수로 해줄 설정이 몇가지 있다

- 타임존
- Character Set (문자 집합) 
- Max Connection (최대 접속 개수)

### 1. 파라미터 생성

일단 이런 설정을 해줄 수 있는 파라미터 그룹을 생성하자

1. RDS 왼쪽 사이드바 > **[파라미터 그룹]** 클릭
2. **[파라미터 그룹 생성]** 클릭해서 파라미터 그룹 세부 정보 설정

    파라미터 그룹 패밀리는 **방금 생성한 RDS의 DB와 같은 버전**으로 설정해야 함  
    그룹 이름과 설명은 원하는 값으로 설정

생성이 완료되면, 파라미터 그룹 목록 창에 새로생긴 그룹 확인!

### 2. 파라미터 편집

파라미터 이름을 클릭하고 **[파라미터 편집]** 클릭하면 편집 모드로 전환된다.

여기서 아까전에 해줘야 했었던 설정값을 변경해주면 된다. 

1. 타임존 변경 
    
    time_zone 항목 검색 후 값을 **Asia/Seoul**로 변경
    
2. character_set 변경

    이건 변경해줄 항목이 많다.  
    character로 검색해서 나오는 아래의 6개 항목의 값을 모두 **utf8mb4**로 변경
    
   - character_set_client
   - character_set_connection
   - character_set_database
   - character_set_filesystem
   - character_set_results
   - character_set_server

    그후, collation을 검색해서 나오는 아래의 2개 항목의 값을 모두 **utf8mb4_general_ci**로 변경
  
   - collation_connection
   - collation_server

3. max_connections
 
    max_connections 항목 검색 후 기본값보다 더 넉넉하게 지정해 준다. (책에서는 150)  
    (현재 프리티어 사양으론 약 60개 커넥션만 가능해서 더 넉넉하게 지정하는 것이라 한다.)
    
    

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

