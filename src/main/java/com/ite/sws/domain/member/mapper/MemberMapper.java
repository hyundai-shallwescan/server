package com.ite.sws.domain.member.mapper;

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
 * </pre>
 */

public interface MemberMapper {

    int selectCountByLoginId(String loginId);
    void insertMember(MemberVO member);
    void insertAuth(AuthVO auth);
    Optional<AuthVO> selectMemberByLoginId(String loginId);
    MemberVO selectMemberByMemberId(Long memberId);
}