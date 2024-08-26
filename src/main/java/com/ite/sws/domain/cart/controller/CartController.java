package com.ite.sws.domain.cart.controller;

import com.ite.sws.domain.cart.dto.GetCartRes;
import com.ite.sws.domain.cart.dto.PostCartItemReq;
import com.ite.sws.domain.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
 * 2024.08.26  	김민정       장바구니에 상품 담기 API 생성
 * </pre>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    /**
     * 장바구니 조회 API
     * @param memberId 멤버 식별자
     * @return 장바구니 아이템 조회 결과 응답
     */
    @GetMapping
    public ResponseEntity<GetCartRes> cartItemList(@RequestParam Long memberId) {
        // TODO: memberId 파라미터 제거
        return ResponseEntity.ok(cartService.findCartItemListByMemberId(memberId));
    }

    /**
     * 장바구니에 상품 담기 API
     * @param postCartItemReq 장바구니 아이템 객체
     * @param memberId 멤버 식별자
     * @return 장바구니 상품 담기 결과 응답
     */
    @PostMapping
    public ResponseEntity<Void> addCartItem(@RequestBody PostCartItemReq postCartItemReq,
                                            @RequestParam Long memberId) {
        // TODO: memberId 파라미터 제거
        cartService.addCartItem(postCartItemReq, memberId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
