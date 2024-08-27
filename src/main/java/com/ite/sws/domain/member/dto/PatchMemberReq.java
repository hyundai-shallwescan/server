package com.ite.sws.domain.member.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 회원 정보 수정 Request DTO
 * @author 정은지
 * @since 2024.08.26
 * @version 1.0
 *
 * <pre>
 * 수정일        	수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	정은지        최초 생성
 * </pre>
 */

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatchMemberReq {

    @Setter
    private Long memberId;

    @NotBlank(message = "필수 입력 항목입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8글자 이상 20글자 이하입니다.")
    private String password;

    @NotBlank(message = "필수 입력 항목입니다.")
    private String phoneNumber;

    @Pattern(regexp = "^[0-9]{2,3}[가-힣][0-9]{4}$", message = "차량 번호가 올바르지 않습니다.")
    private String carNumber;
}
