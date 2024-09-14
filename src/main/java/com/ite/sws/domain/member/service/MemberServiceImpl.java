package com.ite.sws.domain.member.service;

import com.ite.sws.domain.cart.mapper.CartMapper;
import com.ite.sws.domain.cart.service.CartService;
import com.ite.sws.domain.cart.vo.CartMemberVO;
import com.ite.sws.domain.cart.vo.CartVO;
import com.ite.sws.domain.member.dto.*;
import com.ite.sws.domain.member.mapper.MemberMapper;
import com.ite.sws.domain.member.vo.AuthVO;
import com.ite.sws.domain.member.vo.MemberVO;
import com.ite.sws.exception.CustomException;
import com.ite.sws.exception.ErrorCode;
import com.ite.sws.util.JwtTokenProvider;
import com.ite.sws.util.SecurityUtil;
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
import java.util.concurrent.TimeUnit;
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
 * 2024.09.06   남진수        회원가입 시 장바구니 회원도 생성되도록 추가
 * 2024.09.10   남진수        FCM 토큰 저장 기능 추가
 * </pre>
 */

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final CartMapper cartMapper;
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

        // 로그인 아이디 중복 시 예외 발생
        int idCount = memberMapper.selectCountByLoginId(postMemberReq.getLoginId());
        if (idCount > 0) {
            throw new CustomException(ErrorCode.LOGIN_ID_ALREADY_EXISTS);
        }

        // 차량 번호 중복 시 예외 발생
        int carCount = memberMapper.selectCountByCarNumber(postMemberReq.getCarNumber());
        if (carCount > 0) {
            throw new CustomException(ErrorCode.CART_NUMBER_ALREADY_EXISTS);
        }

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

        // Cart_Member 테이블에 데이터 생성
        CartMemberVO cartMember = CartMemberVO.builder()
                .cartId(cart.getCartId())
                .name(member.getName())
                .password(null)
                .build();
        cartMapper.insertCartMember(cartMember);
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

        // 기존 리프레시 토큰 확인
        String existingRefreshToken = redisTemplate.opsForValue().get("REFRESH_TOKEN:" + auth.getMemberId());

        if (existingRefreshToken != null) {
            // 기존 리프레시 토큰이 블랙리스트에 없는 경우 블랙리스트에 추가
            long expirationTime = jwtTokenProvider.getExpiration(existingRefreshToken);
            redisTemplate.opsForValue().set("BLACKLISTED_TOKEN:" + existingRefreshToken, "true", expirationTime, TimeUnit.MILLISECONDS);

            // 기존 리프레시 토큰 삭제
            redisTemplate.delete("REFRESH_TOKEN:" + auth.getMemberId());
        }

        // 관리자 로그인 로직
        if (auth.getRole().equals("ROLE_ADMIN")) {
            return adminLogin(auth, authentication);
            // 사용자 로그인 로직
        } else if (auth.getRole().equals("ROLE_USER")) {
            // FCM 토큰이 없을 경우 예외 처리
            if (postLoginReq.getFcmToken() == null) {
                throw new CustomException(ErrorCode.FCM_TOKEN_MISSING);
            }
            return userLogin(auth, postLoginReq, authentication);
        } else {
            throw new CustomException(ErrorCode.LOGIN_FAIL);
        }
    }

    /**
     * 관리자 로그인 처리
     * @param auth 인증된 사용자 정보
     * @param authentication 인증 정보
     * @return 로그인 응답
     */
    private PostLoginRes adminLogin(AuthVO auth, Authentication authentication) {
        // 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication, auth.getMemberId(), null);
        String accessToken = jwtToken.getAccessToken();
        String refreshToken = jwtToken.getRefreshToken();

        PostLoginRes postLoginRes = PostLoginRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        // Redis에 memberId를 키로 JWT 토큰 저장
//        int expirationMinutes = (int) (jwtTokenProvider.getExpiration(jwtToken.getAccessToken()) / 60000);
//        redisTemplate.opsForValue().set("JWT_TOKEN:" + auth.getMemberId(), token, expirationMinutes);
        redisTemplate.opsForValue().set("REFRESH_TOKEN:" + auth.getMemberId(), jwtToken.getRefreshToken());

        return postLoginRes;
    }

    /**
     * 사용자 로그인 처리
     * @param auth 인증된 사용자 정보
     * @param postLoginReq 로그인 요청 정보
     * @param authentication 인증 정보
     * @return 로그인 응답
     */
    private PostLoginRes userLogin(AuthVO auth, PostLoginReq postLoginReq, Authentication authentication) {
        // cartId 가져오기
        Long cartId = cartService.findCartByMemberId(auth.getMemberId());

        // cartMemberId 가져오기
        Long cartMemberId = cartService.findCartMemberIdByMemberId(auth.getMemberId());

        // 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication, auth.getMemberId(), cartMemberId);
        String accessToken = jwtToken.getAccessToken();
        String refreshToken = jwtToken.getRefreshToken();

        PostLoginRes postLoginRes = PostLoginRes.builder()
                .cartId(cartId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        // Redis에 memberId를 키로 JWT 토큰 및 FCM 토큰 저장
        redisTemplate.opsForValue().set("REFRESH_TOKEN:" + auth.getMemberId(), refreshToken);
        redisTemplate.opsForValue().set("FCM_TOKEN:" + cartMemberId, postLoginReq.getFcmToken());

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
     * @param refreshToken 리프레시 토큰
     */
    @Transactional
    @Override
    public void removeMember(String refreshToken) {
        logout(refreshToken);
        Long memberId = SecurityUtil.getCurrentMemberId();
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
     * @param refreshToken 리프레시 토큰
     */
    @Transactional
    @Override
    public void logout(String refreshToken) {
        if (jwtTokenProvider.validateToken(refreshToken)) {
            long expirationTime = jwtTokenProvider.getExpiration(refreshToken);

            // 리프레시 토큰을 블랙리스트에 추가
            redisTemplate.opsForValue().set("BLACKLISTED_TOKEN:" + refreshToken, "true", expirationTime, TimeUnit.MILLISECONDS);

            // 리프레시 토큰 삭제
            redisTemplate.delete("REFRESH_TOKEN:" + jwtTokenProvider.getMemberIdFromToken(refreshToken));
        }
    }

    /**
     * 리프레시 토큰을 이용한 액세스 토큰 재발급
     * @param refreshToken 리프레시 토큰
     * @return JwtToken 객체
     */
    @Override
    public JwtToken reissueAccessToken(String refreshToken) {
        return jwtTokenProvider.regenerateToken(refreshToken);
    }
}