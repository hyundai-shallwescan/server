package com.ite.sws.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetSalesRes {
    private Long totalAmount;
    private Double maleRatio;
    private Double femaleRatio;
    private Double ageRange1Ratio;
    private Double ageRange2Ratio;
    private Double ageRange3Ratio;
    private Double ageRange4Ratio;
    private Double ageRange5Ratio;
    private Double ageRange6Ratio;
    private Double ageRangeSixtyToLastRatio;
}

