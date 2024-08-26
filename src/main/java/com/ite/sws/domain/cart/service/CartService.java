package com.ite.sws.domain.cart.service;

import com.ite.sws.domain.cart.dto.GetCartRes;
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
 * 2024.08.26  	김민정       MemberId로 장바구니 아이템 조회 기능 추가
 * 2024.08.26  	김민정       장바구니 아이템 생성 기능 추가
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
     * 장바구니 아이템 생성
     * @param postCartItemReq 장바구니 아이템 객체
     */
    void addCartItem(PostCartItemReq postCartItemReq, Long memberId);
}
