package com.ite.sws.domain.member.mapper;

import com.ite.sws.domain.member.dto.GetMemberRes;
import com.ite.sws.domain.member.dto.PatchMemberReq;
import com.ite.sws.domain.member.vo.AuthVO;
import com.ite.sws.domain.member.vo.MemberVO;

import java.util.Optional;

/**
 * 회원 매퍼 인터페이스
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

public interface MemberMapper {

    /**
     * 중복 아이디 체크
     * @param loginId 로그인 아이디
     * @return 로그인 아이디 개수
     */
    int selectCountByLoginId(String loginId);

    /**
     * 회원가입
     * @param member 회원 정보
     */
    void insertMember(MemberVO member);

    /**
     * 회원가입
     * @param auth 인증 정보
     */
    void insertAuth(AuthVO auth);

    /**
     * 로그인
     * @param loginId 로그인 아이디
     * @return Optional<AuthVO> 인증 정보
     */
    Optional<AuthVO> selectMemberByLoginId(String loginId);

    /**
     * 회원 정보 조회
     * @param memberId 멤버 ID (PK)
     * @return GetMemberRes 객체
     */
    GetMemberRes selectMemberByMemberId(Long memberId);

    /**
     * 회원 정보 수정
     * @param patchMemberReq 회원 수정 정보
     */
    void updateMember(PatchMemberReq patchMemberReq);

    /**
     * 회원 탈퇴
     * @param memberId 멤버 ID (PK)
     */
    void updateMemberStatus(Long memberId);
}