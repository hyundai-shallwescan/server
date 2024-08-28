package com.ite.sws.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 동적쿼리를 위한 다양한 검색 조건을 할 수 있는 Criteria 클래스
 * @since 2024.08.26
 * @author 구지웅
 * @version 1.0
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.27   구지웅      최초 생성
 * </pre>
 *
 */
@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class SalesCriteria {
    private int year;
    private int month;
}
