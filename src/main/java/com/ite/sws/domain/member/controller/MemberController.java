package com.ite.sws.domain.member.controller;

import com.ite.sws.domain.member.dto.*;
import com.ite.sws.domain.member.service.MemberService;
import com.ite.sws.exception.CustomException;
import com.ite.sws.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ite.sws.exception.ErrorCode.LOGIN_ID_ALREADY_EXISTS;

/**
 * 회원 컨트롤러
 * @author 정은지
 * @since 2024.08.24
 * @version 1.0
 *
 * <pre>
 * 수정일        	수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.24  	정은지        최초 생성
 * 2024.08.24  	정은지        아이디 중복 확인 및 회원가입 API 생성
 * 2024.08.25   정은지        로그인 API 생성
 * 2024.08.26   정은지        회원 정보 조회 API 생성
 * 2024.08.26   정은지        회원 정보 수정 API 생성
 * 2024.08.26   정은지        회원 탈퇴 기능 API 생성
 * 2024.08.27   정은지        구매 내역 조회 API 생성
 * 2024.08.27   정은지        작성 리뷰 조회 API 생성
 * 2024.08.29   정은지        로그아웃 API 생성
 * </pre>
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(
        value="/members",
        produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
@Log4j2
public class MemberController {

    private final MemberService memberService;

    /**
     * 로그인 아이디 중복 체크 API
     * @param loginId 로그인 아이디
     * @return 중복 여부 응답
     */
    @GetMapping("/check-id")
    public ResponseEntity<?> isLoginIdAvailable(@RequestParam("login-id") String loginId) {
        boolean isAvailable = memberService.isLoginIdAvailable(loginId);

        if (isAvailable) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            throw new CustomException(LOGIN_ID_ALREADY_EXISTS);
        }
    }

    /**
     * 회원가입 API
     * @param postMemberReq 회원 정보 객체
     * @param bindingResult 유효성 검사 결과
     * @return 회원가입 결과 응답
     */
    @PostMapping("/signup")
    public ResponseEntity<?> addMember(@Valid @RequestBody PostMemberReq postMemberReq, BindingResult bindingResult) {

        ResponseEntity<?> errorResponse = handleValidationErrors(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        memberService.addMember(postMemberReq);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 로그인 API
     * @param postLoginReq 로그인 아이디, 비밀번호
     * @return JwtToken 객체
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody PostLoginReq postLoginReq) {

        JwtToken token = memberService.login(postLoginReq);
        return ResponseEntity.ok(token);
    }

    /**
     * 회원 정보 조회 API
     * @return GetMemberRes 객체
     */
    @GetMapping
    public ResponseEntity<GetMemberRes> findMemberByMemberId() {
        Long memberId = SecurityUtil.getCurrentMemberId();
        GetMemberRes member = memberService.findMemberByMemberId(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(member);
    }

    /**
     * 회원 정보 수정 API
     * @param patchMemberReq 회원 수정 정보
     * @param bindingResult 유효성 검사 결과
     */
    @PatchMapping
    public ResponseEntity<?> modifyMember(@Valid @RequestBody PatchMemberReq patchMemberReq, BindingResult bindingResult) {

        ResponseEntity<?> errorResponse = handleValidationErrors(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        Long memberId = SecurityUtil.getCurrentMemberId();
        patchMemberReq.setMemberId(memberId);
        memberService.modifyMember(patchMemberReq);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 회원 탈퇴 API
     * @return 회원 탈퇴 처리 결과 응답
     */
    @DeleteMapping
    public ResponseEntity<Void> modifyMemberStatus() {
        Long memberId = SecurityUtil.getCurrentMemberId();
        memberService.removeMember(memberId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 구매 내역 조회 API
     * @return List<GetMemberPaymentRes> 구매 내역 리스트
     */
    @GetMapping("/payments")
    public ResponseEntity<List<GetMemberPaymentRes>> findPaymentItemList() {
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<GetMemberPaymentRes> paymentHistory = memberService.findPaymentItemList(memberId);

        return ResponseEntity.ok(paymentHistory);
    }

    /**
     * 작성 리뷰 조회 API
     * @param page 페이지 번호 (기본값: 0)
     * @param size 페이지 당 항목 개수 (기본값: 10)
     * @return List<GetMemberReviewRes> 작성한 리뷰 리스트
     */
    @GetMapping("/reviews")
    public ResponseEntity<List<GetMemberReviewRes>> findReviewListByMemberId(@RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<GetMemberReviewRes> reviews = memberService.findReviewList(memberId, page, size);

        return ResponseEntity.ok(reviews);
    }

    /**
     * 로그아웃 API
     * @return 로그아웃 성공 여부
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {

        Long memberId = SecurityUtil.getCurrentMemberId();
        memberService.logout(memberId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 유효성 검사 실패 처리 메서드
     * @param bindingResult
     * @return
     */
    private ResponseEntity<?> handleValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        return null;
    }
}