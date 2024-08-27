package com.ite.sws.domain.cart.service;

import com.ite.sws.domain.cart.mapper.CartMapper;
import com.ite.sws.domain.cart.vo.CartMemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

/**
 * 장바구니 사용자 인증 및 권한 부여를 처리하는 서비스
 * @author 남진수
 * @since 2024.08.27
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.27  남진수	        최초 생성
 * 2024.08.27  남진수        장바구니 유저 정보 로드 서비스 추가
 * </pre>
 */
@RequiredArgsConstructor
@Service("cartUserDetailsService")
public class CartUserDetailsService implements UserDetailsService {

    private final CartMapper cartMapper;

    /**
     * 장바구니 ID를 통해 장바구니 멤버 정보 로드
     * @param loginId 로그인 ID
     * @return CartUserDetails
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return cartMapper.selectCartMemberByLoginId(loginId)
                .map(this::createCartUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(loginId + " 를 찾을 수 없습니다."));
    }

    /**
     * 장바구니 멤버 정보에 권한을 추가하여 UserDetails 객체 생성
     * @param cartMemberVO 장바구니 멤버 정보
     * @return UserDetails 객체
     */
    private UserDetails createCartUserDetails(CartMemberVO cartMemberVO) {
        return User.builder()
                .username(cartMemberVO.getName())  // 로그인 ID
                .password(cartMemberVO.getPassword())  // 암호화된 비밀번호
                .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_CART_USER")))  // CART_USER 권한 부여
                .build();
    }
}
