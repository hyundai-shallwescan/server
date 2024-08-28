package com.ite.sws.domain.checklist.mapper;

import com.ite.sws.domain.checklist.dto.GetShareCheckListRes;
import com.ite.sws.domain.checklist.vo.ShareCheckListItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
 * 2024.08.27  	김민정       cartId에 해당하는 공유 체크리스트 아이템 조회
 * 2024.08.27  	김민정       공유 체크리스트 아이템 생성을 위한 프로시저 호출
 * 2024.08.27  	김민정       공유 체크리스트 아이템 삭제를 위한 프로시저 호출
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
    int selectCountByProductId(@Param("productId") Long productId);

    /**
     * cartId에 해당하는 공유 체크리스트 아이템 조회
     * @param cartId 장바구니 ID
     * @return
     */
    List<GetShareCheckListRes.GetShareCheckRes> selectShareCheckListByCartId(Long cartId);

    /**
     * 공유 체크리스트 아이템 생성을 위한 프로시저 호출
     * @param newItem 공유 체크리스트 아이템 생성 객체
     */
    void insertShareCheckListItem(ShareCheckListItemVO newItem);

    /**
     * 공유 체크리스트 아이템 삭제를 위한 프로시저 호출
     * @param deleteItem 공유 체크리스트 아이템 삭제 객체
     */
    void deleteShareCheckListItem(ShareCheckListItemVO deleteItem);
}
