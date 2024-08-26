package com.ite.sws.domain.cart.dto;

import com.ite.sws.domain.cart.vo.CartItemVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 장바구니 조회 Response DTO
 * @author 김민정
 * @since 2024.08.26
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	김민정       최초 생성
 * </pre>
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GetCartRes {
    private Long cartId;
    private LocalDateTime createdAt;
    private List<CartItemVO> items;
}
