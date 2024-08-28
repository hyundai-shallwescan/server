package com.ite.sws.domain.checklist.mapper;

import com.ite.sws.domain.checklist.dto.GetMyCheckListRes;

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
 * </pre>
 */

public interface MyCheckListMapper {

    /**
     * 마이 체크리스트 조회
     * @param memberId 멤버 ID
     * @return List<GetMyCheckListRes>
     */
    List<GetMyCheckListRes> selectMyCheckListByMemberId(Long memberId);
}
