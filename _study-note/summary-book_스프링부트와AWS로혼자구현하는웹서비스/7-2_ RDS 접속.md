# 7-2. RDS 접속 (로컬 PC에서 + EC2에서)

## 1. RDS 접속전, 보안 그룹 설정

1. 예전에 EC2 보안 그룹 만들때 처럼, 보안 그룹 생성 페이지로 (보안그룹 > [보안 그룹 생성]) 들어감
2. 보안 그룹 이름과 설명은 원하는 값으로, 인바운드 설정을 하기전에...
3. 새 창을 열어서, **EC2에서 쓴 보안 그룹의 그룹 ID**를 복사해 옴
4. 그 보안 그룹 ID와 본인 IP를 **RDS 보안 그룹, 인바운드 규칙의 소스 항목**에 추가, 유형은 **MySQL/Aurora** 선택시 알아서 나머지 값(포트) 설정됨
5. 보안 그룹 생성 후, RDS 설정 들어가서 보안 그룹에 추가해주면 끝!

> ### RDS 보안 그룹, 인바운드 규칙에서 소스 항목에 **EC2 보안 그룹 ID**를 넣는다?
>
> 원래 소스 항목에는 IP 주소를 넣었는데, 이렇게 보안 그룹 ID를 넣으면, 해당 보안 그룹에 해당되는 **EC2와 RDS간에 접근 가능**  
> 이렇게 하면, 이후 EC2가 여러대여도, 해당 보안 그룹을 쓰고 있으면, 연동이 됨

## 2. 로컬 PC에서 RDS 접속 - 인텔리제이 Database 플러그인 이용

로컬에서 원격 DB를 다룰때, GUI 클라이언트(대표적으로 워크밴치, SQLyog(유료), Sequel Pro(맥 전용), DataGrip(유료)) 사용  
-> 본인이 가장 좋아하는 툴 사용하면 됨

근데 여기선 **Database 플러그인** 설치해 진행 (인텔리제이 공식 플러그인은 아님)  

1. RDS 정보 페이지에서 **엔드 포인트** 확인
    
    **'엔드 포인트 = 접근 가능한 URL'** -> 메모장 같은 곳에 복사
    
2. 인텔리제이 **Database 플러그인** 설치 
    
    1. shift 더블 클릭해 All 검색 창 띄우기 
    2. **Plugins** 검색 후, 클릭해 플러그인 창 띄우기
    3. **Database Navigator** 검색 후, **[Install]** 클릭해서 설치
    
3. **DatabaseBrowser** 실행 -> RDS 접속 정보 등록

    1. ctrl+shift+a 으로 Action 검색 창 띄우기 (맥OS는 ctrl대신 command키)
    2. **Database Browser** 검색 후, 실행
    3. 그러면 프로젝트 왼쪽 사이드바에 **DB Browser**가 생김
    4. **[+]** 아이콘 클릭 -> **MySQL** 클릭해서 설정 창 띄우기
    5. 이전에 생성한 RDS 정보를 차례대로 등록
        
        - Name, Description : DB 이름과 설명 등록
        - **Host : RDS의 엔드 포인트** 등록
        - **User, Password : RDS의 마스터 계정명과 비밀번호** 등록
        
    6. **[Test Connection]** 클릭 -> 연결 테스트 실행
    7. *Connection Successful* 메시지 확인 후, **[Apply]**, **[Ok]** 차례대로 클릭해 최종 저장
    
    저장 후, DB Borwer(왼쪽 사이드바 창)에 RDS 스키마가 뜨는 것을 확인하면 끝!
    
4. SQL 콘솔창 실행

    1. **[Open SQL Console]** 클릭(위쪽의 네모 두개 겹쳐있는 아이콘) -> **[New SQL Console...]** 항목 선택 (콘솔 이름은 맘대로)  
    2. 생성된 콘솔창에서 **character_set, collation 설정** 쿼리 실행 
    
        ```mysql-sql
        use '초기 DB 이름';     # 쿼리가 수행될 DB 선택         
        # rds 생성때 지정한 '초기 DB 이름'값 인데, 까먹었어도 왼쪽 사이드바의 'Schemas' 에서 나와있으니 참고
        
        show variables like "c%";   # DB 선택된 상태에서, 현재 c로 시작하는(character_set, collation) 설정 확인
        ```       
        쿼리 실행은 드래그로 선택한 뒤, 위쪽의 **[Execute Statement]**(초록 화살표 아이콘, 단축키 : ctrl + enter) 클릭   
        
        쿼리 실행 결과가 아래에 뜨는데, 필드들 중에 MariaDB에서만 RDS 파라미터 그룹으론 변경 안되는 필드가 있음.  
        -> 아래의 직접 변경하는 쿼리 실행
        
        ```mysql-sql        
        alter database for_springboot
            character set = 'utf8mb4'
            collate = 'utf8mb4_general_ci';
        
        show variables like 'c%';   # 바뀐 결과를 확인
        ```

        **타임존**도 RDS 파라미터 그룹으로 잘 적용됬는지 아래 쿼리로 확인
        ```mysql-sql
        select @@time_zone, now();  # 타임존도 겸사겸사 확인
        ```
        
    3. 테이블 생성, 삽입 쿼리 실행 (한글명 테스트) 
    
        ```mysql-sql
        create table test (
            id  bigint(20)  not null    auto_increment,
            content varchar(255)    default null,
            primary key (id)
        ) ENGINE=innoDB;
        
        insert into test(content) values ('테스트입니다😎');
        
        select * from test;
        # commit 아이콘 클릭하면 실제 DB에 반영된다!
        ```
        위 쿼리를 실행해서, 한글 데이터도 잘 등록되지 확인!
        
        > ### 테이블 생성은 모든 설정이 끝난 후에...
        > 
        > **테이블 생성은 만들어질 당시 설정값을 그대로 유지** -> 자동 변경 X, 강제로 변경해줘야 함
        > 인코딩 설정 전에 테이블 생성을 해도 마찬가지... 
        > 앵간하면 테이블은 모든 설정이 끝난 뒤 생성
    
## 3. EC2에서 RDS 접속

1. 일단 6장에서처럼 ec2에 ssh 접속 (맥: ssh 서비스명, 윈도우: putty)
2. MySQL 접근 테스트를 위해 MySQL CLI 설치
    
    ```shell script
    sudo yum install mysql
    ```

3. 설치 후, 로컬에서 접근하듯이 계정, 비밀번호, 호스트 주소를 사용해 RDS에 접속

    ```shell script
    mysql -u 계정 -p -h Host주소
    ```

4. 패스워드 입력하란 메시지 나오면, 패스워드까지 입력

하면 EC2에서 cli환경으로 RDS에 접속되는 것을 확인!

```mysql-sql
show databases;     # 실제 생성한 RDS가 맞는지 확인 차원으로 쿼리 실행
```
