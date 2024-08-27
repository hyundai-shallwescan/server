package com.ite.sws.domain.checklist.mapper;

import org.apache.ibatis.annotations.Param;


/**
 * 공유 체크리스트 매퍼 인터페이스
 * @author 김민정
 * @since 2024.08.27
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.27  	김민정       최초 생성
 * 2024.08.27  	김민정       장바구니가 존재하는지 확인
 * 2024.08.27  	김민정       상품이 존재하는지 확인
 * </pre>
 */
public interface ShareCheckListMapper {

    /**
     * 장바구니가 존재하는지 확인
     * @param cartId 장바구니 ID
     * @return
     */
    int selectCountByCartId(@Param("cartId") Long cartId);

    /**
     * 상품이 존재하는지 확인
     * @param productId 상품 ID
     * @return
     */
    int selectProductByProductId(@Param("productId") Long productId);
}
