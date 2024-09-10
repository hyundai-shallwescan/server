package com.ite.sws.domain.checklist.service;

import com.ite.sws.domain.checklist.dto.GetMyCheckListRes;
import com.ite.sws.domain.checklist.dto.PostMyCheckListReq;
import com.ite.sws.domain.checklist.dto.PostMyCheckListRes;
import com.ite.sws.domain.checklist.dto.PutMyCheckListReq;

import java.util.List;

/**
 * 마이 체크리스트 서비스
 * @author 정은지
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	정은지       최초 생성
 * 2024.08.28  	정은지       마이 체크리스트 조회 추가
 * 2024.08.28   정은지       마이 체크리스트 아이템 추가
 * 2024.08.28   정은지       마이 체크리스트 아이템 체크 상태 변경
 * 2024.08.28   정은지       마이 체크리스트 아이템 삭제
 * 2024.08.28   정은지       마이 체크리스트 아이템 변경
 * </pre>
 */

public interface MyCheckListService {

    /**
     * 마이 체크리스트 조회
     * @param memberId 멤버 ID
     * @return List<GetMyCheckListRes>
     */
    List<GetMyCheckListRes> findMyCheckList(Long memberId);

    /**
     * 마이 체크리스트 아이템 추가
     * @param postMyCheckListReq 추가 아이템
     */
    PostMyCheckListRes addMyCheckListItem(PostMyCheckListReq postMyCheckListReq);

    /**
     * 마이 체크리스트 아이템 체크 상태 변경
     * @param myCheckListItemId 아이템 ID
     */
    void modifyMyCheckListItemStatus(Long myCheckListItemId);

    /**
     * 마이 체크리스트 아이템 삭제
     * @param myCheckListItemId 아이템 ID
     */
    void removeMyCheckListItem(Long myCheckListItemId);

    /**
     * * 마이 체크리스트 아이템 변경
     * @param myCheckListItemId 마이 체크리스트 ID
     * @param putMyCheckListReq 아이템 변경 정보
     */
    void modifyMyCheckListItem(Long myCheckListItemId, PutMyCheckListReq putMyCheckListReq);
}
