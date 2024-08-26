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
 * 2024.08.26  	김민정       장바구니 아이템 추가 및 수량 증가 기능 추가
 * 2024.08.26  	김민정       장바구니 아이템 수량 변경
 * </pre>
 */
public interface CartService {

    /**
     * MemberId로 장바구니 아이템 조회
     * @param memberId 멤버 식별자
     * @return 장바구니 아이템 리스트
     */
    GetCartRes findCartItemList(Long memberId);

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
}
