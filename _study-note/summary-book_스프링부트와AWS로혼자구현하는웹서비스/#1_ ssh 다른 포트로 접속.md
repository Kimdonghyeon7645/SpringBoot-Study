# #1. ssh 다른 포트로 접속

    우리학교에서 ssh 22포트 접속을 막아놨다...  
    다른 포트로 ssh를 접속하는 것도 공부해보라는, 학교의 큰뜻일까

### 1. sshd 설정 변경
    
```shell script
sudo vim /etc/ssh/sshd_config
# 으로, sshd 설정을 편집기로 열었을 때
# '# Port 22'라는 주석된 포트 설정을 찾아주자.
# 그리고 주석 풀고 원하는 포트로 변경해주자.

sudo service sshd restart   # 또는 sudo /etc/init.d/sshd restart
# 해서 sshd를 재시작 해주자
# Restarting sshd (via systemctl):                           [  OK  ]
# 위같은 값이 출력되면 끝
```

### 2. 방화벽 설정 변경

방화벽이 있으면, 그 값도 변경해줘야 되는데  
AWS는 방화벽 대신 **보안 그룹** 이 있다.

보안 그룹에서 원하는 포트도 허용해주면 된다.

> [참고한 곳](http://blog.nuriware.com/archives/301)
