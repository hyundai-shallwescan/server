package com.ite.sws.domain.cart.controller;

import com.ite.sws.domain.cart.dto.GetCartRes;
import com.ite.sws.domain.cart.service.CartService;
import com.ite.sws.domain.member.dto.JwtToken;
import com.ite.sws.domain.member.dto.PostLoginReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 장바구니 로그인 및 회원가입
     * @param postLoginReq 아이디, 비밀번호
     * @return JwtToken 객체
     */
    @PostMapping("/login")
    public ResponseEntity<?> findMemberByLoginId(@RequestBody PostLoginReq postLoginReq) {
        JwtToken token = cartService.findCartMemberByLoginId(postLoginReq);
        return ResponseEntity.ok(token);
    }
}
