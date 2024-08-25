package com.ite.sws.domain.member.service;

import com.ite.sws.domain.member.dto.JwtToken;
import com.ite.sws.domain.member.dto.PostLoginReq;
import com.ite.sws.domain.member.dto.PostMemberReq;

/**
 * 회원 서비스
 * @author 정은지
 * @since 2024.08.24
 * @version 1.0
 *
 * <pre>
 * 수정일        	수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.24  	정은지        최초 생성
 * 2024.08.24   정은지        중복 아이디 체크 및 회원가입 기능 추가
 * </pre>
 */

public interface MemberService {

    /**
     * 로그인 아이디 중복 체크
     * @param loginId 아이디
     * @return 아이디 중복 여부
     */
    boolean isLoginIdAvailable(String loginId);

    /**
     * 회원가입
     * @param postMemberReq 회원 정보
     */
    void registerMember(PostMemberReq postMemberReq);
}
