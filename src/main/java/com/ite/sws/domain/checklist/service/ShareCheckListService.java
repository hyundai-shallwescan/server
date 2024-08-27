package com.ite.sws.domain.checklist.service;

import com.ite.sws.domain.checklist.dto.GetShareCheckListRes;

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
 * </pre>
 */
public interface ShareCheckListService {

    /**
     * cartId로 공유 체크리스트 아이템 조회
     * @param cartId 장바구니 ID
     * @return 공유 체크리스트 아이템 리스트
     */
    GetShareCheckListRes findShareCheckList(Long cartId);
}
