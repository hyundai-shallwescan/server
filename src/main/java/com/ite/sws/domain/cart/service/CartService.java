package com.ite.sws.domain.cart.service;

import com.ite.sws.domain.cart.dto.GetCartRes;
import com.ite.sws.domain.member.dto.JwtToken;
import com.ite.sws.domain.member.dto.PostLoginReq;
import com.ite.sws.domain.cart.dto.PostCartItemReq;

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
 * 2024.08.26  	김민정       cartId로 장바구니 아이템 조회 기능 추가
 * 2024.08.26  	김민정       장바구니 아이템 추가 및 수량 증가 기능 추가
 * 2024.08.26  	김민정       장바구니 아이템 수량 변경
 * 2024.08.26  	김민정       장바구니 아이템 삭제
 * 2024.08.26   남진수       장바구니 로그인 및 회원가입 기능 추가
 * 2024.09.01  	김민정       MemberId로 장바구니 조회: private->public 변경
 * </pre>
 */
public interface CartService {

    /**
     * cartId로 장바구니 아이템 조회
     * @param cartId 장바구니 PK
     * @return 장바구니 아이템 리스트
     */
    GetCartRes findCartItemList(Long cartId);

    /**
     * 장바구니 아이템 추가 및 수량 증가
     * @param postCartItemReq 장바구니 아이템 객체
     */
    void addAndModifyCartItem(PostCartItemReq postCartItemReq, Long memberId);

    /**
     * 장바구니 아이템 수량 변경
     * @param cartId 장바구니 ID
     * @param productId 상품 ID
     * @param delta 수량 변화량 (+1, -1)
     */
    void modifyCartItemQuantity(Long cartId, Long productId, int delta);

    /**
     * 장바구니 아이템 삭제
     * @param cartId 장바구니 ID
     * @param productId 상품 ID
     */
    void removeCartItem(Long cartId, Long productId);
  
    /**
     * 장바구니 로그인 및 회원가입
     * @param postLoginReq 아이디, 비밀번호
     * @return JwtToken 객체
     */
    JwtToken findCartMemberByLoginId(PostLoginReq postLoginReq);

    /**
     * MemberId로 장바구니 조회
     * @param memberId 멤버 식별자
     * @return 멤버의 장바구니 식별자
     */
    Long findCartByMemberId(Long memberId);
}
