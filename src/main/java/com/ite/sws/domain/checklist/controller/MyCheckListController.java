package com.ite.sws.domain.checklist.controller;

import com.ite.sws.domain.checklist.dto.GetMyCheckListRes;
import com.ite.sws.domain.checklist.dto.PostMyCheckListReq;
import com.ite.sws.domain.checklist.dto.PutMyCheckListReq;
import com.ite.sws.domain.checklist.service.MyCheckListService;
import com.ite.sws.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 마이 체크리스트 컨트롤러
 * @author 정은지
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        	수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	정은지        최초 생성
 * 2024.08.28   정은지        마이 체크리스트 조회 API 생성
 * 2024.08.28   정은지        마이 체크리스트 아이템 추가 API 생성
 * 2024.08.28   정은지        마이 체크리스트 아이템 체크 상태 변경 API 생성
 * 2024.08.28   정은지        마이 체크리스트 아이템 삭제 API 생성
 * 2024.08.28   정은지        마이 체크리스트 아이템 변경 API 생성
 * </pre>
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(
        value="/my-checklist",
        produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
@Log4j2
public class MyCheckListController {

    private final MyCheckListService myCheckListService;

    /**
     * 마이 체크리스트 조회 API
     * @return List<GetMyCheckListRes> 마이 체크리스트 객체
     */
    @GetMapping
    public ResponseEntity<List<GetMyCheckListRes>> findMyCheckList() {

        Long memberId = SecurityUtil.getCurrentMemberId();
        List<GetMyCheckListRes> myCheckList = myCheckListService.findMyCheckList(memberId);

        return ResponseEntity.ok(myCheckList);
    }

    /**
     * 마이 체크리스트 아이템 추가 API
     * @param postMyCheckListReq 마이 체크리스트 아이템
     * @return 아이템 추가 성공 여부
     */
    @PostMapping
    public ResponseEntity<Void> addMyCheckListItem(@RequestBody PostMyCheckListReq postMyCheckListReq) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        postMyCheckListReq.setMemberId(memberId);

        myCheckListService.addMyCheckListItem(postMyCheckListReq);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 마이 체크리스트 아이템 상태 변경 API (CHECK -> UNCHECK, UNCHECK -> CHECK)
     * @param myCheckListItemId 마이 체크리스트 아이템 ID
     * @return 아이템 상태 변경 성공 여부
     */
    @PatchMapping("/{myCheckListItemId}")
    public ResponseEntity<Void> modifyMyCheckListItemStatus(@PathVariable Long myCheckListItemId) {

        myCheckListService.modifyMyCheckListItemStatus(myCheckListItemId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 마이 체크리스트 아이템 삭제 API
     * @param myCheckListItemId 마이 체크리스트 아이템 ID
     * @return 아이템 삭제 성공 여부
     */
    @DeleteMapping("/{myCheckListItemId}")
    public ResponseEntity<Void> deleteMyCheckListItem(@PathVariable Long myCheckListItemId) {

        myCheckListService.removeMyCheckListItem(myCheckListItemId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 마이 체크리스트 아이템 변경 API
     * @param myCheckListItemId 마이 체크리스트 아이템 ID
     * @param putMyCheckListReq 아이템 변경 정보
     * @return 아이템 변경 성공 여부
     */
    @PutMapping("/{myCheckListItemId}")
    public ResponseEntity<Void> modifyMyCheckListItem(@PathVariable Long myCheckListItemId,
                                                      @RequestBody PutMyCheckListReq putMyCheckListReq) {

        myCheckListService.modifyMyCheckListItem(myCheckListItemId, putMyCheckListReq);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}