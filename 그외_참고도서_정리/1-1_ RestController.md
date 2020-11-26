# 1-1. RestController

- ### **RestController** : *REST*를 위한 전용 컨트롤러  
-> 대부분 HTML 대신 **텍스트, JSON**으로 정보 전송  
```java
@RestController
public class HelloController { 
    @RequestMapping("/hello")
    // @GetMapping("/hello")  // @RequestMapping(method = RequestMethod)와 같음
    public String hello() {
        return "힘쎄고 강한 아침! 만일 내게 물어보면 나는 SpringBoot.";
    }
}
```

- **@RequestMapping("주소")** : 인자값으로 지정한 주소로 접속(요청) -> 해당 어노테이션이 있는 메소드(리퀘스트 핸들러)가 자동 실행
    > 컨트롤러(웹 요청을 실제로 처리하는 객체)를 스프링 MVC입장에선 핸들러(Handler)라 표기 [(참고)](https://articles09.tistory.com/32)

- **@PathVariable** : URL에서 매개변수를 받아 해당 어노테이션이 붙은 변수에 저장  
    ```java
    @RequestMapping("/{num}")
    public String handler(@PathVariable int num) {
        // 리퀘스트(요청) 핸들링(처리)
    }
    ```

- 객체 JSON 출력 : RestController는 자바 객체를 JSON 형식 텍스트로 자동변환해 출력  
-> 스프링부트에선 객체를 반환하기만 하면 됨
    ```java
    @RequestMapping("/{id}")
    public DataObject index(@PathVariable int id) {
        return new DataObject(id, names[id], mails[id]);
    }
    ```
