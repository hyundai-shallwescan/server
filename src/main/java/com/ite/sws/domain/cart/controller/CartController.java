package com.ite.sws.domain.cart.controller;

import com.ite.sws.domain.cart.dto.GetCartRes;
import com.ite.sws.domain.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
 * </pre>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<GetCartRes> cartItemList(@RequestParam Long memberId) {
        return ResponseEntity.ok(cartService.findCartItemListByMemberId(memberId));
    }
}
