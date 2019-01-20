package com.shahidfoy.reactiveblog.security;


import com.shahidfoy.reactiveblog.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import reactor.core.publisher.Mono;

@Configuration
class SecurityConfig {

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {

        //@formatter:off
        return http
                .csrf().disable()
                .httpBasic().securityContextRepository(new WebSessionServerSecurityContextRepository())
                .and()
                .authorizeExchange()
                .pathMatchers(HttpMethod.GET, "/posts/**").permitAll()
                .pathMatchers(HttpMethod.DELETE, "/posts/**").hasRole("ADMIN")
                .pathMatchers("/posts/**").authenticated()
                .pathMatchers("/user").authenticated()
                .pathMatchers("/users/{user}/**").access(this::currentUserMatchesPath)
                .anyExchange().permitAll()
                .and()
                .build();
        //@formatter:on
    }

    private Mono<AuthorizationDecision> currentUserMatchesPath(Mono<Authentication> authentication, AuthorizationContext context) {
        return authentication
                .map(a -> context.getVariables().get("user").equals(a.getName()))
                .map(AuthorizationDecision::new);
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService(UserRepository userRepository) {
        return (username) -> userRepository.findByUsername(username)
                .map(u -> User.withUsername(u.getUsername())
                        .password(u.getPassword())
                        .authorities(u.getRoles().toArray(new String[0]))
                        .accountExpired(!u.isActive())
                        .credentialsExpired(!u.isActive())
                        .disabled(!u.isActive())
                        .accountLocked(!u.isActive())
                        .build()
                );
    }

    @Bean
    PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
