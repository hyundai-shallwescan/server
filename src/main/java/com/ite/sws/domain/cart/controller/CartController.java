package com.ite.sws.domain.cart.controller;

import com.ite.sws.domain.cart.dto.GetCartRes;
import com.ite.sws.domain.cart.dto.PostCartItemReq;
import com.ite.sws.domain.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 장바구니 컨트롤러
 * @author 김민정
 * @since 2024.08.26
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	김민정       최초 생성
 * 2024.08.26  	김민정       장바구니 조회 API 생성
 * 2024.08.26  	김민정       장바구니 항목 추가 및 수량 증가 API 생성
 * 2024.08.26  	김민정       장바구니 수량 변경 API 생성
 * </pre>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    /**
     * 장바구니 조회 API
     * @param memberId 멤버 ID
     * @return 장바구니 아이템 조회 결과 응답
     */
    @GetMapping
    public ResponseEntity<GetCartRes> findCartItemList(@RequestParam Long memberId) {
        // TODO: memberId 파라미터 제거
        return ResponseEntity.ok(cartService.findCartItemList(memberId));
    }

    /**
     * 장바구니 항목 추가 및 수량 증가 API
     * @param postCartItemReq 장바구니 아이템 객체
     * @param memberId 멤버 ID
     * @return 장바구니 상품 담기 결과 응답
     */
    @PutMapping
    public ResponseEntity<Void> addAndModifyCartItem(@RequestBody PostCartItemReq postCartItemReq,
                                                     @RequestParam Long memberId) {
        // TODO: memberId 파라미터 제거
        cartService.addAndModifyCartItem(postCartItemReq, memberId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 장바구니 아이템 수량 변경
     * @param cartId 장바구니 ID
     * @param productId 상품 ID
     * @param delta 수량 변화량 (+1, -1)
     * @return 장바구니 아이템 수량 변경 결과 응답
     */
    @PatchMapping("/{cartId}/products/{productId}")
    public ResponseEntity<Void> modifyCartItemQuantity(@PathVariable Long cartId,
                                                       @PathVariable Long productId,
                                                       @RequestParam int delta) {
        cartService.modifyCartItemQuantity(cartId, productId, delta);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
