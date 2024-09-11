package com.ite.sws.domain.cart.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;

/**
 * 장바구니 로그인 Request DTO
 * @author 남진수
 * @since 2024.09.06
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.06  남진수       최초 생성
 * 2024.09.10  남진수       안드로이드 device 토큰 추가
 * </pre>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCartLoginReq {
    @NotBlank
    private String loginId;
    @NotBlank
    private String password;
    private Long cartId;
    @Setter
    private String fcmToken;
}
