package com.ite.sws.domain.checklist.service;

import com.ite.sws.domain.checklist.dto.GetMyCheckListRes;

import java.util.List;

/**
 * 마이 체크리스트 서비스
 * @author 정은지
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	정은지       최초 생성
 * 2024.08.28  	정은지       마이 체크리스트 조회 추가
 * </pre>
 */

public interface MyCheckListService {

    /**
     * 마이 체크리스트 아이템 조회 기능
     * @param memberId 멤버 ID
     * @return List<GetMyCheckListRes>
     */
    List<GetMyCheckListRes> findMyCheckList(Long memberId);
}
