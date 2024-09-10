package com.ite.sws.domain.checklist.service;


import com.ite.sws.domain.checklist.dto.GetMyCheckListRes;
import com.ite.sws.domain.checklist.dto.PostMyCheckListReq;
import com.ite.sws.domain.checklist.dto.PostMyCheckListRes;
import com.ite.sws.domain.checklist.dto.PutMyCheckListReq;
import com.ite.sws.domain.checklist.mapper.MyCheckListMapper;
import com.ite.sws.domain.checklist.vo.MyCheckListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 마이 체크리스트 서비스 구현체
 * @author 정은지
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	정은지       최초 생성
 * 2024.08.28  	정은지       마이 체크리스트 조회
 * 2024.08.28   정은지       마이 체크리스트 아이템 추가
 * 2024.08.28   정은지       마이 체크리스트 아이템 체크 상태 변경
 * 2024.08.28   정은지       마이 체크리스트 아이템 삭제
 * 2024.08.28   정은지       마이 체크리스트 아이템 변경
 * </pre>
 */

@Service
@RequiredArgsConstructor
public class MyCheckListServiceImpl implements MyCheckListService{

    private final MyCheckListMapper myCheckListMapper;

    /**
     * 마이 체크리스트 조회
     * @param memberId 멤버 ID
     * @return List<GetMyCheckListRes>
     */
    @Transactional(readOnly = true)
    @Override
    public List<GetMyCheckListRes> findMyCheckList(Long memberId) {

        return myCheckListMapper.selectMyCheckListByMemberId(memberId);
    }

    /**
     * 마이 체크리스트 아이템 추가
     * @param postMyCheckListReq 추가 아이템
     */
    @Transactional
    @Override
    public PostMyCheckListRes addMyCheckListItem(PostMyCheckListReq postMyCheckListReq) {

        MyCheckListVO myCheckList = MyCheckListVO.builder()
                    .memberId(postMyCheckListReq.getMemberId())
                    .item(postMyCheckListReq.getItem())
                    .build();

        myCheckListMapper.insertMyCheckListItem(myCheckList);

        return PostMyCheckListRes.builder()
                .myCheckListItemId(myCheckList.getMyCheckListItemId())
                .build();
    }

    /**
     * 마이 체크리스트 아이템 체크 상태 변경
     * @param myCheckListItemId 아이템 ID
     */
    @Transactional
    @Override
    public void modifyMyCheckListItemStatus(Long myCheckListItemId) {

        myCheckListMapper.updateMyCheckListItemStatus(myCheckListItemId);
    }

    /**
     * 마이 체크리스트 아이템 삭제
     * @param myCheckListItemId 아이템 ID
     */
    @Transactional
    @Override
    public void removeMyCheckListItem(Long myCheckListItemId) {

        myCheckListMapper.deleteMyCheckListItem(myCheckListItemId);
    }

    /**
     * * 마이 체크리스트 아이템 변경
     * @param myCheckListItemId 마이 체크리스트 ID
     * @param putMyCheckListReq 아이템 변경 정보
     */
    @Transactional
    @Override
    public void modifyMyCheckListItem(Long myCheckListItemId, PutMyCheckListReq putMyCheckListReq) {

        MyCheckListVO myCheckList = MyCheckListVO.builder()
                        .myCheckListItemId(myCheckListItemId)
                        .item(putMyCheckListReq.getItem())
                        .build();

        myCheckListMapper.updateMyCheckListItem(myCheckList);
    }
}
