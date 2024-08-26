package com.ite.sws.domain.member.controller;

import com.ite.sws.domain.member.dto.JwtToken;
import com.ite.sws.domain.member.dto.PostLoginReq;
import com.ite.sws.domain.member.dto.PostMemberReq;
import com.ite.sws.domain.member.service.MemberService;
import com.ite.sws.domain.member.vo.MemberVO;
import com.ite.sws.exception.CustomException;
import com.ite.sws.exception.ErrorResponse;
import com.ite.sws.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 로그인 아이디 중복 체크
     * @param loginId 로그인 아이디
     * @return 중복 여부 응답
     */
    @GetMapping("/check-id")
    public ResponseEntity<?> checkLoginId(@RequestParam("login-id") String loginId) {
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
    public ResponseEntity<?> signup(@Valid @RequestBody PostMemberReq postMemberReq, BindingResult bindingResult) {

        // 유효성 검사 실패 시
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors().stream()
                    .map(error -> String.format("{\"field\":\"%s\", \"message\":\"%s\"}", error.getField(), error.getDefaultMessage()))
                    .collect(Collectors.joining(", "));

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .errorCode("VALIDATION_ERROR")
                    .message(String.format("[%s]", errors))
                    .build();

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            memberService.registerMember(postMemberReq);
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
     * @return JwtToken
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody PostLoginReq postLoginReq) {
        JwtToken token = memberService.login(postLoginReq);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(@RequestHeader("Authorization") String authorizationHeader) {
        // Bearer 토큰에서 실제 JWT 토큰 부분만 추출
        String token = authorizationHeader.substring(7);

        // 토큰에서 memberId 추출
        Long memberId = jwtTokenProvider.getMemberIdFromToken(token);

        log.info("Extracted memberId: {}", memberId);

        MemberVO member = memberService.getMemberById(memberId);

        // 추출한 memberId를 이용하여 필요한 작업 수행
        return ResponseEntity.status(HttpStatus.OK).body(member);
    }
}