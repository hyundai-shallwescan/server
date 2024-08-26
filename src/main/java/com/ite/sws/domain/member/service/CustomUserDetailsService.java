package com.ite.sws.domain.member.service;

import com.ite.sws.domain.member.mapper.MemberMapper;
import com.ite.sws.domain.member.vo.AuthVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

/**
 * 사용자 인증 및 권한 부여를 처리하는 서비스
 * @author 정은지
 * @since 2024.08.25
 * @version 1.0
 *
 * <pre>
 * 수정일        	수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.25  	정은지        최초 생성
 * </pre>
 */

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;

    /**
     * 사용자 ID를 통해 사용자 정보 로드
     * @param loginId
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return memberMapper.selectMemberByLoginId(loginId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(loginId + " 를 찾을 수 없습니다."));
    }

    /**
     * 사용자 정보에 권한을 추가하여 UserDetails 객체 생성
     * @param authVO 사용자 인증 정보 객체
     * @return UserDetails 객체
     */
    private UserDetails createUserDetails(AuthVO authVO) {
        return User.builder()
                .username(authVO.getLoginId())  // 로그인 ID
                .password(authVO.getPassword())  // 암호화된 비밀번호
                .authorities(Collections.singleton(new SimpleGrantedAuthority(authVO.getRole())))  // 사용자 권한
                .build();
    }
}