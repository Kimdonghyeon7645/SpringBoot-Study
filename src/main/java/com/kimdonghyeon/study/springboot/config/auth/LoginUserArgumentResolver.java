/*
 스프링의 ArgumentResolver :
    컨트롤러 메소드에서 특정 조건에 맞는 매개변수(파라미터)가 있는지 검사해서, 조건에 맞다면 그 매개변수에 원하는 값을 바인딩(=대입=전달) 해줌

 HandlerMethodArgumentResolver :
    ArgumentResolver를 만들고 싶을 때 구현해야 되는 인터페이스
 HandlerMethodArgumentResolver 쓰이는 예 :
    @PathVariable으로 Request의 Path파라미터 값을 받아올 때,
    @RequestBody로 Request의 Body값을 받아올 때 사용

 HandlerMethodArgumentResolver를 구현하는 객체는 2개의 메소드를 구현해야 함
 
 1. public boolean supportsParameter(MethodParameter parameter) :
    파라미터가 특정 조건에 맞는지를 판별 (true/false로 반환) 
    (true면 파라미터가 조건에 맞으니, resolveArgument() 메소드를 실행)
 2. resolveArgument() :
    파라미터가 특정 조건에 맞으면(= supportsParameter()의 반환값이 true면),
    해당 파라미터에 바인딩(=대입, 전달)할 객체를 반환

 이러면 새로운 ArgumentResolver를 만들 수 있으며,
 스프링이 새로 만든 ArgumentResolver를 인식할 수 있도록 WebMvcConfigurer에 추가해야 함
 */
package com.kimdonghyeon.study.springboot.config.auth;

import com.kimdonghyeon.study.springboot.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    /*
     여기서는 LoginUserArgumentResolver 라는 ArgumentResolver 를 만듬
     이걸로 @LoginUser 어노테이션이 있으며, 타입이 SessionUser 인 컨트롤러 메소드의 매개변수(파라미터)를 (판별은 supportsParameter() 메소드로 함)
     resolveArgument() 메소드의 반환 값으로 바인딩(=대입=전달) 한다.
     */

    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());

        return isLoginUserAnnotation && isUserClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return httpSession.getAttribute("user");
    }
}
