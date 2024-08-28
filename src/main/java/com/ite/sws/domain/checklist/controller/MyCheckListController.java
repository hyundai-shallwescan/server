package com.ite.sws.domain.checklist.controller;

import com.ite.sws.domain.checklist.dto.GetMyCheckListRes;
import com.ite.sws.domain.checklist.service.MyCheckListService;
import com.ite.sws.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public ResponseEntity<?> findMyCheckList() {

        Long memberId = SecurityUtil.getCurrentMemberId();
        List<GetMyCheckListRes> myCheckList = myCheckListService.findMyCheckList(memberId);

        return ResponseEntity.ok(myCheckList);
    }
}
