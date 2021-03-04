/* CustomOAuth2UserService
 이 클래스에서는 세션 로그인에서 가져온 사용자의 정보(email, name, picture 등)을 기반으로,
 가입, 정보 수정, 세션 저장등의 기능을 지원
 */
package com.kimdonghyeon.study.springboot.config.auth;

import com.kimdonghyeon.study.springboot.config.auth.dto.OAuthAttributes;
import com.kimdonghyeon.study.springboot.config.auth.dto.SessionUser;
import com.kimdonghyeon.study.springboot.domain.user.User;
import com.kimdonghyeon.study.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        /* registrationId
         현재 로그인 진행중인 서비스를 구분하는 코드
         이후에 구글 말고도 네이버 로그인도 연동할 때, 둘 중에 어떤 로그인인지 구분하기 위해 사용
         */
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        /* userNameAttributeName
         OAuth2 로그인 진행시 키가 되는 필드 값 (DB의 PK와 같은 의미)
         구글의 경우 기본적으로 코드를 지원(기본 코드 = "sub")하지만, 네이버/카카오 등은 기본 지원 안함
         이후에 구글 말고도 네이버 로그인도 연동할 때, 사용
         */

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        /* OAuthAttributes
         OAuth2UserService를 통해 가져온 OAuth2User의 attribute(속성)을 담을 클래스 (필자는 Dto로 구분)
         이후 다른 소셜 로그인도 이 클래스 사용
         */

        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user));
        /* SessionUser
         세션에 사용자 정보를 저장하기 위한 Dto 클래스
         왜 사용자 정보가 있는 User 클래스를 그대로 안 쓰고 이걸로 쓰는지는 SessionUser 클래스 주석으로 설명
         */

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())   // 이메일로 유저를 찾아서,
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))    // 유저 이름과 사진을 업데이트
                .orElse(attributes.toEntity()); // 유저가 없으면 생성

        return userRepository.save(user);
    }
}
