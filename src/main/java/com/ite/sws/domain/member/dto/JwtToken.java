package com.ite.sws.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * JWT 토큰 DTO
 * @author 정은지
 * @since 2024.08.25
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.25   정은지        최초 생성
 * </pre>
 */

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {

    private String grantType;
    private String accessToken;
    private String refreshToken;
}