package com.ite.sws.domain.checklist.service;

import com.ite.sws.domain.checklist.dto.GetShareCheckListRes;
import com.ite.sws.domain.checklist.dto.PostShareCheckListReq;

/**
 * 공유 체크리스트 서비스
 * @author 김민정
 * @since 2024.08.27
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.27  	김민정       최초 생성
 * 2024.08.27  	김민정       cartId로 공유 체크리스트 아이템 조회 기능 추가
 * 2024.08.27  	김민정       공유 체크리스트에 아이템 생성 기능 추가
 * 2024.08.28  	김민정       공유 체크리스트 아이템 삭제 기능 추가
 * 2024.08.28  	김민정       공유 체크리스트 아이템 체크 상태 변경 기능 추가
 * </pre>
 */
public interface ShareCheckListService {

    /**
     * cartId로 공유 체크리스트 아이템 조회
     * @param cartId 장바구니 ID
     * @return 공유 체크리스트 아이템 리스트
     */
    GetShareCheckListRes findShareCheckList(Long cartId);

    /**
     * 공유 체크리스트에 아이템 생성
     * @param postShareCheckListReq 공유 체크리스트 아이템 객체
     */
    void addShareCheckListItem(PostShareCheckListReq postShareCheckListReq);

    /**
     * 공유 체크리스트 아이템 삭제
     * @param cartId 장바구니 PK
     * @param productId 상품 PK
     */
    void removeShareCheckListItem(Long cartId, Long productId);

    /**
     * 공유 체크리스트 아이템 체크 상태 변경
     * @param cartId 장바구니 PK
     * @param productId 상품 PK
     */
    void modifyShareCheckListItem(Long cartId, Long productId);
}
