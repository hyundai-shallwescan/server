package com.ite.sws.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * SecurityContext 에서 현재 인증된 사용자의 정보를 가져오는 유틸리티 클래스
 * @author 정은지
 * @since 2024.08.25
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.25   정은지        최초 생성
 * 2024.09.01   정은지        cartId 추출 메서드 추가
 * </pre>
 */

public class SecurityUtil {

    /**
     * 인증된 사용자의 CustomAuthenticationDetails를 가져오는 메서드
     * @return JwtTokenProvider.CustomAuthenticationDetails 인증된 사용자의 CustomAuthenticationDetails 객체
     */
    private static JwtTokenProvider.CustomAuthenticationDetails getAuthenticationDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof JwtTokenProvider.CustomAuthenticationDetails) {
            return (JwtTokenProvider.CustomAuthenticationDetails) authentication.getDetails();
        }
        return null;
    }

    /**
     * memberId를 추출하는 메서드
     * @return Long memberId
     */
    public static Long getCurrentMemberId() {
        JwtTokenProvider.CustomAuthenticationDetails details = getAuthenticationDetails();
        return (details != null) ? details.getMemberId() : null;
    }

    /**
     * cartMemberId를 추출하는 메서드
     * @return Long cartMemberId
     */
    public static Long getCurrentCartMemberId() {
        JwtTokenProvider.CustomAuthenticationDetails details = getAuthenticationDetails();
        return (details != null) ? details.getCartMemberId() : null;
    }
}