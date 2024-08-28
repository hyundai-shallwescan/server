package com.ite.sws.domain.checklist.controller;

import com.ite.sws.domain.checklist.dto.GetShareCheckListRes;
import com.ite.sws.domain.checklist.dto.PostShareCheckListReq;
import com.ite.sws.domain.checklist.service.ShareCheckListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
 * 2024.08.27  	김민정       공유 체크리스트 아이템 추가 API 생성
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
    @GetMapping("/{cartId}")
    public ResponseEntity<GetShareCheckListRes> findShareCheckList(@PathVariable Long cartId) {
        return ResponseEntity.ok(shareCheckListService.findShareCheckList(cartId));
    }

    /**
     * 공유 체크리스트 아이템 추가 API
     * @param postShareCheckListReq 공유 체크리스트 아이템 객체
     * @return 공유 체크리스트 아이템 추가 결과 응답
     */
    @PostMapping
    public ResponseEntity<Void> addShareCheckListItem(@RequestBody PostShareCheckListReq postShareCheckListReq) {
        shareCheckListService.addShareCheckListItem(postShareCheckListReq);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
