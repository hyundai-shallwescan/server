package com.ite.sws.domain.cart.service;

import com.ite.sws.domain.cart.dto.GetCartRes;
import com.ite.sws.domain.member.dto.JwtToken;
import com.ite.sws.domain.member.dto.PostLoginReq;

/**
 * 장바구니 서비스
 * @author 김민정
 * @since 2024.08.26
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	김민정       최초 생성
 * 2024.08.26  	김민정       MemberId로 장바구니 아이템 조회 기능 추가
 * </pre>
 */
public interface CartService {

    /**
     * MemberId로 장바구니 아이템 조회
     * @param memberId 멤버 식별자
     * @return 장바구니 아이템 리스트
     */
    GetCartRes findCartItemListByMemberId(Long memberId);

    /**
     * 장바구니 로그인 및 회원가입
     * @param postLoginReq 아이디, 비밀번호
     * @return JwtToken 객체
     */
    JwtToken findCartMemberByLoginId(PostLoginReq postLoginReq);
}
