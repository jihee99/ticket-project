package com.ex.hero.security.jwt;

import com.ex.hero.member.model.dto.request.SignInRequest;
import com.ex.hero.member.model.dto.response.SignInRequestValidationResult;
import com.ex.hero.member.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final SignService signService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
            String principal = (String) authenticationToken.getPrincipal();
            String credential = (String) authenticationToken.getCredentials();

            SignInRequestValidationResult signInRequestValidationResult = signService.validateSignInRequest(new SignInRequest(principal, credential));

            return new UsernamePasswordAuthenticationToken(
                principal, null,
                AuthorityUtils.createAuthorityList(signInRequestValidationResult.getRoleKey())
            );
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ClassUtils.isAssignable(UsernamePasswordAuthenticationToken.class, authentication);
    }
}