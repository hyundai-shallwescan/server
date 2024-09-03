package com.ite.sws.domain.member.service;

import com.ite.sws.domain.cart.service.CartService;
import com.ite.sws.domain.cart.vo.CartVO;
import com.ite.sws.domain.member.dto.*;
import com.ite.sws.domain.member.mapper.MemberMapper;
import com.ite.sws.domain.member.vo.AuthVO;
import com.ite.sws.domain.member.vo.MemberVO;
import com.ite.sws.exception.CustomException;
import com.ite.sws.exception.ErrorCode;
import com.ite.sws.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 회원 서비스 구현체
 * @author 정은지
 * @since 2024.08.24
 * @version 1.0
 *
 * <pre>
 * 수정일        	수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.24  	정은지        최초 생성
 * 2024.08.24   정은지        중복 아이디 체크 및 회원가입 추가
 * 2024.08.25   정은지        로그인 추가
 * 2024.08.26   정은지        회원 정보 조회 추가
 * 2024.08.26   정은지        회원 정보 수정 추가
 * 2024.08.26   정은지        회원 탈퇴 추가
 * 2024.08.27   남진수        AuthenticationManager로 변경
 * 2024.08.27   정은지        회원가입 로직 수정
 * 2024.08.27   정은지        구매 내역 조회 추가
 * 2024.08.27   정은지        작성 리뷰 조회 추가
 * 2024.08.29   정은지        로그아웃 추가
 * 2024.09.01   정은지        로그인 반환 값에 cartId 추가
 * </pre>
 */

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final CartService cartService;

    /**
     * 로그인 아이디 중복 체크
     * @param loginId 아이디
     * @return 아이디 중복 여부
     */
    @Transactional(readOnly = true)
    @Override
    public boolean isLoginIdAvailable(String loginId) {
        return memberMapper.selectCountByLoginId(loginId) == 0;
    }

    /**
     * 회원가입
     * @param postMemberReq 회원 정보
     */
    @Transactional
    @Override
    public void addMember(PostMemberReq postMemberReq) {

        // Member 테이블에 데이터 생성
        MemberVO member = MemberVO.builder()
                .name(postMemberReq.getName())
                .gender(postMemberReq.getGender())
                .age(postMemberReq.getAge())
                .phoneNumber(postMemberReq.getPhoneNumber())
                .carNumber(postMemberReq.getCarNumber())
                .build();

        memberMapper.insertMember(member);

        // Auth 테이블에 데이터 생성
        AuthVO auth = AuthVO.builder()
                .memberId(member.getMemberId())
                .loginId(postMemberReq.getLoginId())
                .password(passwordEncoder.encode(postMemberReq.getPassword()))
                .build();

        memberMapper.insertAuth(auth);

        // Cart 테이블에 데이터 생성
        CartVO cart = CartVO.builder()
                .memberId(member.getMemberId())
                .build();

        memberMapper.insertCart(cart);
    }

    /**
     * 로그인
     * @param postLoginReq 아이디, 비밀번호
     * @return JwtToken 객체
     */
    @Transactional
    @Override
    public PostLoginRes login(PostLoginReq postLoginReq) {

        // 로그인 아이디로 사용자 조회
        Optional<AuthVO> authOptional = memberMapper.selectMemberByLoginId(postLoginReq.getLoginId());

        // 사용자가 존재하지 않으면 예외 발생
        if(!authOptional.isPresent()) {
            throw new CustomException(ErrorCode.LOGIN_FAIL);
        }

        // 비밀번호가 일치하지 않으면 예외 발생
        AuthVO auth = authOptional.get();
        if(!passwordEncoder.matches(postLoginReq.getPassword(), auth.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAIL);
        }

        // 아이디와 비밀번호를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(postLoginReq.getLoginId(), postLoginReq.getPassword());

        // 실제 검증
        // authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // cartId 가져오기
        Long cartId = cartService.findCartByMemberId(auth.getMemberId());

        // 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication, auth.getMemberId());
        String token = jwtToken.getAccessToken().toString();

        PostLoginRes postLoginRes =  PostLoginRes.builder()
                .cartId(cartId)
                .token(token)
                .build();

        // Redis에 memberId를 키로 JWT 토큰 저장

        int expirationMinutes = (int) (jwtTokenProvider.getExpiration(jwtToken.getAccessToken()) / 60000);
        redisTemplate.opsForValue().set("JWT_TOKEN:" + auth.getMemberId(), token, expirationMinutes);

        return postLoginRes;
    }

    /**
     * 멤버 아이디로 회원 정보 조회
     * @param memberId 멤버 아이디
     * @return MemberVO 객체
     */
    @Transactional(readOnly = true)
    @Override
    public GetMemberRes findMemberByMemberId(Long memberId) {

        return memberMapper.selectMemberByMemberId(memberId);
    }

    /**
     * 회원 정보 수정
     * @param patchMemberReq 회원 수정 정보
     */
    @Transactional
    @Override
    public void modifyMember(PatchMemberReq patchMemberReq) {

        PatchMemberReq member = PatchMemberReq.builder()
                .memberId(patchMemberReq.getMemberId())
                .password(passwordEncoder.encode(patchMemberReq.getPassword()))
                .phoneNumber(patchMemberReq.getPhoneNumber())
                .carNumber(patchMemberReq.getCarNumber())
                .build();
        memberMapper.updateMember(member);
    }

    /**
     * 회원 탈퇴
     * @param memberId 멤버 ID (PK)
     */
    @Transactional
    @Override
    public void removeMember(Long memberId) {
        memberMapper.deleteMember(memberId);
    }

    /**
     * 구매 내역 조회
     * @param memberId 멤버 ID
     * @return 구매 내역 리스트
     */
    @Transactional(readOnly = true)
    @Override
    public List<GetMemberPaymentRes> findPaymentItemList(Long memberId) {
        List<GetMemberPaymentRes> paymentList = memberMapper.selectPaymentListByMemberID(memberId);

        return paymentList.stream()
                .map(payment -> {
                    List<GetMemberPaymentRes.GetMemberPaymentItemRes> items = memberMapper.selectPaymentItemByPaymentId(payment.getPaymentId());
                    return GetMemberPaymentRes.builder()
                            .paymentId(payment.getPaymentId())
                            .createdAt(payment.getCreatedAt())
                            .amount(payment.getAmount())
                            .items(items != null ? items : Collections.emptyList())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 작성 리뷰 조회
     * @param memberId 멤버 ID
     * @return 리뷰 리스트
     */
    @Transactional(readOnly = true)
    @Override
    public List<GetMemberReviewRes> findReviewList(Long memberId, int page, int size) {
        int offset = page * size;
        return memberMapper.selectReviewListByMemberId(memberId, offset, size);
    }

    /**
     * 로그아웃
     * @param memberId 멤버 ID
     */
    @Transactional
    @Override
    public void logout(Long memberId) {
        String key = "JWT_TOKEN:" + memberId;
        // RedisUtil을 사용하여 해당 토큰이 존재하는지 확인
        if (redisTemplate.hasKey(key)) {
            // 해당 토큰이 존재하면 삭제하여 로그아웃 처리
            redisTemplate.delete(key); // Token 삭제
        }
    }
}