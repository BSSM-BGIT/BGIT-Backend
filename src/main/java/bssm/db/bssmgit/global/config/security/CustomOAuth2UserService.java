package bssm.db.bssmgit.global.config.security;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.domain.type.Role;
import bssm.db.bssmgit.domain.user.repository.UserRepository;
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

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService{

    private final UserRepository userRepository;
    private final HttpSession session;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        DefaultOAuth2UserService service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest);

        User user = saveOrUpdate(oAuth2User);

        session.setAttribute("oAuthToken", userRequest.getAccessToken().getTokenValue());

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                oAuth2User.getAttributes(), "login");
        //nameAttributeKey = Principal name에 저장됨
    }

    public User saveOrUpdate(OAuth2User oAuth2User) {
        User user = User.builder()
                .githubId(oAuth2User.getAttribute("login"))
                .role(Role.ROLE_BSSM)
                .build();

        return userRepository.save(user);
    }
}