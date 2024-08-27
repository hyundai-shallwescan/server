package com.ite.sws.domain.checklist.controller;

import com.ite.sws.domain.checklist.dto.GetShareCheckListRes;
import com.ite.sws.domain.checklist.service.ShareCheckListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 공유 체크리스트 컨트롤러
 * @author 김민정
 * @since 2024.08.27
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.27  	김민정       최초 생성
 * 2024.08.27  	김민정       공유 체크리스트 조회 API 생성
 * </pre>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/share-checklist")
public class ShareCheckListController {

    private final ShareCheckListService shareCheckListService;

    /**
     * 공유 체크리스트 조회 API
     * @param cartId 장바구니 ID
     * @return 공유 체크리스트 조회 결과 응답
     */
    @GetMapping
    public ResponseEntity<GetShareCheckListRes> findShareCheckList(@PathVariable Long cartId) {
        return ResponseEntity.ok(shareCheckListService.findShareCheckList(cartId));
    }
}
