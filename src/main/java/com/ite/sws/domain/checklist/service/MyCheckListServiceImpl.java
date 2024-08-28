package com.ite.sws.domain.checklist.service;


import com.ite.sws.domain.checklist.dto.GetMyCheckListRes;
import com.ite.sws.domain.checklist.mapper.MyCheckListMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 마이 체크리스트 서비스 구현체
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

@Service
@RequiredArgsConstructor
public class MyCheckListServiceImpl implements MyCheckListService{

    private final MyCheckListMapper myCheckListMapper;

    /**
     * 마이 체크리스트 아이템 조회
     * @param memberId 멤버 ID
     * @return List<GetMyCheckListRes>
     */
    @Override
    public List<GetMyCheckListRes> findMyCheckList(Long memberId) {

        return myCheckListMapper.selectMyCheckListByMemberId(memberId);
    }
}
