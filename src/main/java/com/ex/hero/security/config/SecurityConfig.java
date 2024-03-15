package com.ex.hero.security.config;

import com.ex.hero.member.repository.MemberRepository;
import com.ex.hero.security.filter.JwtAuthorizationFilter;
import com.ex.hero.security.filter.NewJwtAuthenticationFilter;
import com.ex.hero.security.jwt.TokenProviderUp;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final String[] allowedUrls = {"/swagger-ui/**", "/v3/**", "/sign-up", "/sign-in"};
//	private final AuthenticationEntryPoint entryPoint;
//	private final CorsFilter corsFilter;
	private final AuthenticationConfiguration authenticationConfiguration;
	private final TokenProviderUp tokenProvider;
	private final MemberRepository memberRepository;
//	private final AccessDeniedFilter accessDeniedFilter;

//	private final FilterConfig filterConfig;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		/*
		*  HTML, CSS, JavaScript, image 등 정적 자원에 대해 보안을 적용하지 않도록 설정
		* */
		return web -> web.ignoring()
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}


	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChainForActuator(HttpSecurity http) throws Exception {

		http
			.csrf(AbstractHttpConfigurer::disable)
//			.addFilterBefore(corsFilter, ChannelProcessingFilter.class)

			.formLogin((formLogin) -> formLogin.disable())
			.httpBasic((httpBasic) -> httpBasic.disable())

			.sessionManagement( sessionManageMent -> sessionManageMent.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(requests ->
				requests
					.requestMatchers(allowedUrls).permitAll()

					.requestMatchers("/api/v[0-9]+/member/**").hasAnyAuthority("USER", "MASTER", "MANAGER", "ADMIN")
					.requestMatchers("/api/v[0-9]+/orders/**").hasAnyAuthority("USER", "MASTER", "MANAGER", "ADMIN")
					.requestMatchers("/api/v[0-9]+/group/**").hasAnyAuthority("MASTER", "MANAGER", "ADMIN") // seller, admin 권한 허용
					.requestMatchers("/api/v[0-1]+/group").hasAnyAuthority("USER", "MASTER", "MANAGER", "ADMIN")
					.requestMatchers("/api/v[0-9]+/manager/**").hasAnyAuthority("MASTER", "MANAGER", "ADMIN")
					.requestMatchers("/api/v[0-9]+/master/**").hasAnyAuthority("MASTER", "ADMIN") // seller, admin 권한 허용
					.requestMatchers("/api/v[0-9]+/system/**").hasAuthority("ADMIN") // admin 권한 허용

					.anyRequest().authenticated() // 그 외의 모든 요청은 인증 필요
			);


//		.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//		.addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class);


//			.addFilterBefore(accessDeniedFilter, FilterSecurityInterceptor.class)
//			.exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint))	// 추가

		http.addFilterBefore(new NewJwtAuthenticationFilter(
				authenticationManager(authenticationConfiguration), tokenProvider), UsernamePasswordAuthenticationFilter.class);

		http.addFilterBefore(new JwtAuthorizationFilter(
				authenticationManager(authenticationConfiguration), tokenProvider, memberRepository), BasicAuthenticationFilter.class);

//		http.apply(filterConfig);

		return http.build();
	}

	// @Bean
	// public RoleHierarchy roleHierarchy() {
	// 	RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
	// 	hierarchy.setHierarchy("ROLE_ADMIN > ROLE_HOST > ROLE_MANAGER > ROLE_USER");
	// 	return hierarchy;
	// }
	//
	// @Bean
	// static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
	// 	DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
	// 	expressionHandler.setRoleHierarchy(roleHierarchy);
	// 	return expressionHandler;
	// }


}
