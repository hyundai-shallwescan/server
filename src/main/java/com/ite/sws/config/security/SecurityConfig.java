package com.ite.sws.config.security;

import com.ite.sws.domain.cart.service.CartUserDetailsService;
import com.ite.sws.domain.member.service.CustomUserDetailsService;
import com.ite.sws.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정 클래스
 * @author 정은지
 * @since 2024.08.24
 * @version 1.0
 *
 * <pre>
 * 수정일        	수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.24  	정은지        최초 생성
 * 2024.08.27  	남진수        HttpSecurity 장바구니 설정 추가
 * </pre>
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private final CartUserDetailsService cartUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final RedisTemplate<String, String> redisTemplate;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManagerBuilder 설정
     * 사용자 인증을 위한 UserDetailsService와 PasswordEncoder 설정
     * @param auth AuthenticationManagerBuilder 객체
     * @throws Exception exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());

        auth.userDetailsService(cartUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    /**
     * HttpSecurity 설정
     * HTTP 보안 설정을 구성하고 JWT 필터를 추가
     * @param http HttpSecurity 객체
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.httpBasic().disable()
                .formLogin().disable()
                .authorizeRequests()
                .antMatchers("/members").hasRole("USER")
                .antMatchers("/**").permitAll();
//                .antMatchers("/members/signup", "/members/check-id", "/members/login", "/members/logout").permitAll()
//                .antMatchers("/admins/**").hasRole("ADMIN")
//                .antMatchers("/carts/**").hasRole("CART_USER")
//                .anyRequest().hasAnyRole("ADMIN", "USER");
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisTemplate), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * AuthenticationManager 빈 등록
     * @return AuthenticationManager
     * @throws Exception exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}