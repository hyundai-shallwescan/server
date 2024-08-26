package com.ite.sws.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


/**
 * 회원가입 Request DTO
 * @author 정은지
 * @since 2024.08.24
 * @version 1.0
 *
 * <pre>
 * 수정일        	수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.24  	정은지        최초 생성
 * </pre>
 */

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostMemberReq {

    @NotBlank(message = "필수 입력 항목입니다.")
    @Size(min = 4, max = 20, message = "아이디는 4글자 이상 20글자 이하입니다.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "아이디는 영문 소문자와 숫자만 입력 가능합니다.")
    private String loginId;

    @NotBlank(message = "필수 입력 항목입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8글자 이상 20글자 이하입니다.")
    private String password;

    @NotBlank(message = "필수 입력 항목입니다.")
    private String name;

    @NotNull(message = "필수 입력 항목입니다.")
    private Character gender;

    @NotNull(message = "필수 입력 항목입니다.")
    private Integer age;

    @NotBlank(message = "필수 입력 항목입니다.")
    private String phoneNumber;

    @Pattern(regexp = "^[0-9]{2,3}[가-힣][0-9]{4}$", message = "차량 번호가 올바르지 않습니다.")
    private String carNumber;
}
