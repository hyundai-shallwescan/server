package com.ite.sws.domain.checklist.mapper;

import com.ite.sws.domain.checklist.dto.GetMyCheckListRes;
import com.ite.sws.domain.checklist.vo.MyCheckListVO;

import java.util.List;

/**
 * 마이 체크리스트 매퍼 인터페이스
 * @author 정은지
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        	수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	정은지        최초 생성
 * 2024.08.28   정은지        마이 체크리스트 조회 추가
 * 2024.08.28   정은지        아이템 추가
 * 2024.08.28   정은지        아이템 체크 상태 변경
 * </pre>
 */

public interface MyCheckListMapper {

    /**
     * 마이 체크리스트 조회
     * @param memberId 멤버 ID
     * @return List<GetMyCheckListRes>
     */
    List<GetMyCheckListRes> selectMyCheckListByMemberId(Long memberId);

    /**
     * 아이템 추가
     * @param myCheckList MyCheckListVO 객체
     */
    void insertMyCheckListItem(MyCheckListVO myCheckList);

    /**
     * 아이템 체크 상태 변경
     * @param myCheckListItemId 아이템 ID
     */
    void updateItemStatus(Long myCheckListItemId);
}
