package com.ite.sws.domain.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * CartItemList DTO
 * @author 남진수
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	남진수       최초 생성
 * </pre>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemListDTO {
    private int quantity;
    private int isDeleted;
    private int price;
}
