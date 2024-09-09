package com.ite.sws.domain.cart.controller;

import com.ite.sws.domain.cart.dto.GetCartRes;
import com.ite.sws.domain.cart.dto.PostCartItemReq;
import com.ite.sws.domain.cart.dto.PostCartLoginReq;
import com.ite.sws.domain.cart.service.CartService;
import com.ite.sws.domain.member.dto.JwtToken;
import com.ite.sws.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.DeleteMapping;
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
 * 2024.08.26   남진수       장바구니 로그인 API 생성
 * 2024.08.26  	김민정       장바구니 아이템 추가 API 생성
 * 2024.08.26  	김민정       장바구니 수량 변경 API 생성
 * 2024.08.26  	김민정       장바구니 아이템 삭제 API 생성
 * 2024.08.31  	김민정       PathVariable에서 memberId 제거
 * 2024.09.05   김민정       장바구니 상태 변화 웹소켓으로 전송
 * </pre>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    private final SimpMessagingTemplate template;

    /**
     * 장바구니 조회 API
     * @return 장바구니 아이템 조회 결과 응답
     */
    @GetMapping("/{cartId}")
    public ResponseEntity<GetCartRes> findCartItemList(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartService.findCartItemList(cartId));
    }
  
    /**
     * 장바구니 로그인 및 회원가입
     * @param postCartLoginReq 아이디, 비밀번호
     * @return JwtToken 객체
     */
    @PostMapping("/login")
    public ResponseEntity<?> cartLogin(@RequestBody PostCartLoginReq postCartLoginReq) {
        JwtToken token = cartService.cartLogin(postCartLoginReq);
        return ResponseEntity.ok(token);
    }

    /**
     * 장바구니 아이템 추가 API
     * @param postCartItemReq 장바구니 아이템 객체
     * @return 장바구니 상품 담기 결과 응답
     */
    @PutMapping
    public ResponseEntity<Void> addAndModifyCartItem(@RequestBody PostCartItemReq postCartItemReq) {
        Long memberId = SecurityUtil.getCurrentMemberId();
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

    /**
     * 장바구니 아이템 삭제
     * @param cartId 장바구니 ID
     * @param productId 상품 ID
     * @return 장바구니 아이템 삭제 결과 응답
     */
    @DeleteMapping("/{cartId}/products/{productId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartId,
                                               @PathVariable Long productId) {
        cartService.removeCartItem(cartId, productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
