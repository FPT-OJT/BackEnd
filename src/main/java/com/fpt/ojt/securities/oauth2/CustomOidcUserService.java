package com.fpt.ojt.securities.oauth2;

import com.fpt.ojt.models.User;
import com.fpt.ojt.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {
    private final UserService userService;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        User userEntity = userService.handleUpdateGoogleCredential(
                oidcUser.getAttribute("sub"),
                oidcUser.getAttribute("email"),
                oidcUser.getAttribute("given_name"),
                oidcUser.getAttribute("family_name"),
                oidcUser.getAttribute("picture")
        );

        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(userEntity.getRole().getValue())
        );

        return new UserPrincipal(
                userEntity.getId(),
                userEntity.getEmail(),
                authorities,
                oidcUser.getAttributes()
        );
    }
}
