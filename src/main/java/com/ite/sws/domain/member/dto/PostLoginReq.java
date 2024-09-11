package com.ite.sws.domain.member.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * 로그인 Request DTO
 * @author 정은지
 * @since 2024.08.25
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.25   정은지        최초 생성
 * 2024.09.10   남진수        안드로이드 device 토큰 추가
 * </pre>
 */

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostLoginReq {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;

    @Setter
    private String fcmToken;
}
