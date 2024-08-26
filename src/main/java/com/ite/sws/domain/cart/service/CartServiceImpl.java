package com.ite.sws.domain.cart.service;

import com.ite.sws.domain.cart.dto.GetCartRes;
import com.ite.sws.domain.cart.dto.PostCartItemReq;
import com.ite.sws.domain.cart.mapper.CartMapper;
import com.ite.sws.domain.cart.vo.CartItemVO;
import com.ite.sws.domain.cart.vo.CartVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 장바구니 서비스 구현체
 * @author 김민정
 * @since 2024.08.26
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	김민정       최초 생성
 * 2024.08.26  	김민정       MemberId로 장바구니 아이템 조회 기능 추가
 * 2024.08.26   김민정       새로운 장바구니 생성 기능 추가
 * 2024.08.26   김민정       MemberId로 장바구니 조회 기능 추가
 * 2024.08.26  	김민정       장바구니 아이템 추가 및 수량 증가 기능 추가
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;

    /**
     * MemberId로 장바구니 아이템 조회
     * @param memberId 멤버 식별자
     * @return 장바구니 아이템 리스트
     */
    @Override
    @Transactional
    public GetCartRes findCartItemList(Long memberId) {
        Long cartId = findCartByMemberId(memberId);

        // 해당 cart_id에 속하는 cart items 가져오기
        List<GetCartRes.GetCartItemRes> cartItems = cartMapper.selectCartItemListByCartId(cartId);

        return GetCartRes.builder()
                .cartId(cartId)
                .items(cartItems)
                .build();
    }

    /**
     * 장바구니 아이템 추가 및 수량 증가
     * (1) 기존에 장바구니에 해당 상품이 존재하지 않을 시, 새로운 아이템 생성
     * (2) 기존에 장바구니에 해당 상품이 존재할 시, 수량 증가
     * @param postCartItemReq 장바구니 아이템 객체
     */
    @Override
    @Transactional
    public void addAndModifyCartItem(PostCartItemReq postCartItemReq, Long memberId) {
        // 유저의 장바구니 조회
        Long cartId = findCartByMemberId(memberId);

        // 바코드 번호로 상품 아이디 조회
        Long productId = cartMapper.selectProductByBarcode(postCartItemReq.getBarcode());

        // 장바구니 아이템 생성 시 필요한 데이터 설정
        CartItemVO newCartItem = CartItemVO.builder()
                .cartId(cartId)
                .productId(productId)
                .build();
        cartMapper.insertCartItem(newCartItem);
    }

    /**
     * MemberId로 장바구니 조회
     * @param memberId 멤버 식별자
     * @return 멤버의 장바구니 식별자
     */
    private Long findCartByMemberId(Long memberId) {
        // 장바구니에서 ACTIVE인 상태 중 가장 최근에 생성된 cart 가져오기
        Long cartId = cartMapper.selectActiveCartByMemberId(memberId);

        // 해당 유저의 장바구니가 없을 시, 장바구니 생성
        if (cartId == null) {
            cartId = addNewCart(memberId);
        }

        return cartId;
    }

    /**
     * 새로운 장바구니 생성
     * @param memberId 멤버 식별자
     * @return 새로 생성된 장바구니 ID
     */
    private Long addNewCart(Long memberId) {
        // 장바구니 생성 시 필요한 데이터 설정
        CartVO newCart = CartVO.builder()
                .memberId(memberId)
                .build();

        // 장바구니 생성
        cartMapper.insertCart(newCart);

        // 새로 생성된 장바구니의 ID 반환
        return newCart.getCartId();
    }
}
