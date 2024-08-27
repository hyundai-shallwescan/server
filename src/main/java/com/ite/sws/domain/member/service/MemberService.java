package com.ite.sws.domain.member.service;

import com.ite.sws.domain.member.dto.*;

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
 * 2024.08.25   정은지        로그인 기능 추가
 * 2024.08.26   정은지        회원 정보 조회 기능 추가
 * 2024.08.26   정은지        회원 정보 수정 기능 추가
 * 2024.08.26   정은지        회원 탈퇴 기능 추가
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
    void addMember(PostMemberReq postMemberReq);

    /**
     * 로그인
     * @param postLoginReq 아이디, 비밀번호
     * @return JwtToken 객체
     */
    JwtToken findMemberByLoginId(PostLoginReq postLoginReq);

    /**
     * 멤버 아이디로 회원 정보 조회
     * @param memberId 멤버 ID (PK)
     * @return GetMemberRes 객체
     */
    GetMemberRes findMemberByMemberId(Long memberId);

    /**
     * 회원 정보 수정
     * @param patchMemberReq 회원 수정 정보
     */
    void modifyMember(PatchMemberReq patchMemberReq);

    /**
     * 회원 탈퇴
     * @param memberId 멤버 ID (PK)
     */
    void modifyMemberStatus(Long memberId);
}