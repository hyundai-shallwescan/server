package com.ite.sws.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Spring Security 설정 클래스
 *
 * @author 정은지
 * @since 2024.08.24
 * @version 1.0
 *
 * <pre>
 * 수정일        	수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.24  	정은지        최초 생성
 * </pre>
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.httpBasic().disable()
                .formLogin().disable()
                .authorizeRequests()
                .antMatchers("/**").permitAll();
//                .antMatchers("/members/signup", "/members/check-id", "/members/login", "/members/logout").permitAll()
//                .antMatchers("/members/test").hasRole("USER");
//                .antMatchers("/admins/**").hasRole("ADMIN")
//                .anyRequest().hasAnyRole("ADMIN", "USER");
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}