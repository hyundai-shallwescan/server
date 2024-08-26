package com.ite.sws.domain.cart.mapper;

import com.ite.sws.domain.cart.vo.CartItemVO;
import com.ite.sws.domain.cart.vo.CartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 장바구니 매퍼 인터페이스
 * @author 김민정
 * @since 2024.08.26
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	김민정       최초 생성
 * 2024.08.26  	김민정       ACTIVE 상태인 가장 최신의 Cart ID를 가져오기 기능 추가
 * 2024.08.26  	김민정       Cart ID에 해당하는 장바구니 아이템 가져오기 기능 추가
 * 2024.08.26  	김민정       새로운 장바구니 생성
 * </pre>
 */
public interface CartMapper {

    /**
     * ACTIVE 상태인 가장 최신의 Cart ID를 가져오기
     * @param memberId 멤버 ID
     * @return 장바구니 ID
     */
    Long selectActiveCartByMemberId(@Param("memberId") Long memberId);

    /**
     * Cart ID에 해당하는 장바구니 아이템 가져오기
     * @param cartId 장바구니 ID
     * @return 장바구니 아이템 리스트
     */
    List<CartItemVO> selectCartItemsByCartId(@Param("cartId") Long cartId);

    /**
     * 새로운 장바구니 생성
     * @param cart 장바구니 객체
     */
    void insertCart(CartVO cart);
}
