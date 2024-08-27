package com.ite.sws.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 정보 조회 Request DTO
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
public class GetMemberRes {

    private String loginId;
    private String name;
    private Character gender;
    private Integer age;
    private String phoneNumber;
    private String carNumber;
}