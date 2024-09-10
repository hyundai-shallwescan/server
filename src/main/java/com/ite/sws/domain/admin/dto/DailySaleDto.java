package com.ite.sws.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 일별 세일즈 조회를 위한 DTO
 * @since 2024.08.26
 * @author 구지웅
 * @version 1.0
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.09.04  	구지웅      최초 생성
 * </pre>
 *
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DailySaleDto {
    private Integer day;
    private Long totalAmount;
}
