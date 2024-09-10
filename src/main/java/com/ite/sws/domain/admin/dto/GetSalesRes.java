package com.ite.sws.domain.admin.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * sales 조회를 위한 DTO
 * @since 2024.08.26
 * @author 구지웅
 * @version 1.0
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.27  	구지웅      최초 생성
 * 2024.09.04  	구지웅       DailySalesDtoList 추가
 * </pre>
 *
 */


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetSalesRes {

    private List<DailySaleDto> dailySaleDtoList;
    private Double maleRatio;
    private Double femaleRatio;
    private Double ageRange1Ratio;
    private Double ageRange2Ratio;
    private Double ageRange3Ratio;
    private Double ageRange4Ratio;
    private Double ageRange5Ratio;
    private Double ageRangeSixtyToLastRatio;


}
