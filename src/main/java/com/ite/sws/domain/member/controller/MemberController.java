package com.ite.sws.domain.member.controller;

import com.ite.sws.domain.member.dto.JwtToken;
import com.ite.sws.domain.member.dto.PostLoginReq;
import com.ite.sws.domain.member.dto.PostMemberReq;
import com.ite.sws.domain.member.service.MemberService;
import com.ite.sws.domain.member.vo.MemberVO;
import com.ite.sws.exception.CustomException;
import com.ite.sws.exception.ErrorResponse;
import com.ite.sws.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ite.sws.exception.ErrorCode.INTERNAL_SERVER_ERROR;
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
     * 로그인 아이디 중복 체크
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
     * 회원가입
     * @param postMemberReq 회원 정보 객체
     * @param bindingResult 유효성 검사 결과
     * @return 회원가입 결과 응답
     */
    @PostMapping("/signup")
    public ResponseEntity<?> addMember(@Valid @RequestBody PostMemberReq postMemberReq, BindingResult bindingResult) {

        // 유효성 검사 실패 시
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            fieldError -> fieldError.getField(),
                            fieldError -> fieldError.getDefaultMessage()
                    ));

            ErrorResponse errorResponse = ErrorResponse.create(
                    HttpStatus.BAD_REQUEST.value(),
                    "VALIDATION_ERROR",
                    errors
            );

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            memberService.addMember(postMemberReq);
            log.info("회원가입 성공 : {}", postMemberReq.getLoginId());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            log.error("회원가입 실패: {}", e.getMessage());
            throw new CustomException(INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 로그인
     * @param postLoginReq 로그인 아이디, 비밀번호
     * @return JwtToken 객체
     */
    @PostMapping("/login")
    public ResponseEntity<?> findMemberByLoginId(@RequestBody PostLoginReq postLoginReq) {

        log.info("LOGIN 정보", postLoginReq);

        JwtToken token = memberService.findMemberByLoginId(postLoginReq);
        return ResponseEntity.ok(token);
    }

    /**
     * 회원 정보 조회 테스트
     * @return MemberVO
     */
    @GetMapping("/test")
    public ResponseEntity<MemberVO> test() {
        Long memberId = SecurityUtil.getCurrentMemberId();
        MemberVO member = memberService.getMemberById(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(member);
    }
}