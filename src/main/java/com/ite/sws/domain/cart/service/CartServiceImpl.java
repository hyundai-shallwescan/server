package com.ite.sws.domain.cart.service;

import com.ite.sws.domain.cart.dto.GetCartRes;
import com.ite.sws.domain.cart.mapper.CartMapper;
import com.ite.sws.domain.cart.vo.CartItemVO;
import com.ite.sws.domain.cart.vo.CartMemberVO;
import com.ite.sws.domain.cart.vo.CartVO;
import com.ite.sws.domain.member.dto.JwtToken;
import com.ite.sws.domain.member.dto.PostLoginReq;
import com.ite.sws.exception.CustomException;
import com.ite.sws.exception.ErrorCode;
import com.ite.sws.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * 장바구니 서비스 구현체
 * @author 김민정
 * @since 2024.08.26
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	김민정       최초 생성
 * 2024.08.26  	김민정       MemberId로 장바구니 아이템 조회 기능 추가
 * 2024.08.26   김민정       새로운 장바구니 생성 기능 추가
 * 2024.08.26   남진수       장바구니 로그인 기능 추가
 * 2024.08.27   남진수       CartMember 생성 메서드 추가 (장바구니 회원가입)
 * 2024.08.27   남진수       인증 및 토큰 생성 메서드 추가
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * MemberId로 장바구니 아이템 조회
     * @param memberId 멤버 식별자
     * @return 장바구니 아이템 리스트
     */
    @Override
    @Transactional
    public GetCartRes findCartItemListByMemberId(Long memberId) {
        // 장바구니에서 ACTIVE인 상태 중 가장 최근에 생성된 cart 가져오기
        Long cartId = cartMapper.selectActiveCartByMemberId(memberId);


        // 해당 유저의 장바구니가 없을 시, 장바구니 생성
        if (cartId == null) {
            cartId = createNewCart(memberId);
        }

        // 해당 cart_id에 속하는 cart items 가져오기
        List<CartItemVO> cartItems = cartMapper.selectCartItemsByCartId(cartId);

        return GetCartRes.builder()
                .cartId(cartId)
                .items(cartItems)
                .build();
    }

    /**
     * 새로운 장바구니 생성
     * @param memberId 멤버 식별자
     * @return 새로 생성된 장바구니 ID
     */
    private Long createNewCart(Long memberId) {
        // 장바구니 생성 시 필요한 데이터 설정
        CartVO newCart = CartVO.builder()
                .memberId(memberId)
                .build();

        // 장바구니 생성
        cartMapper.insertCart(newCart);

        // 새로 생성된 장바구니의 ID 반환
        return newCart.getCartId();
    }

    /**
     * 장바구니 로그인 및 회원가입
     * @param postLoginReq 아이디, 비밀번호
     * @return JwtToken 객체
     */
    @Transactional
    public JwtToken findCartMemberByLoginId(PostLoginReq postLoginReq) {
        Optional<CartMemberVO> authOptional = cartMapper.selectCartMemberByLoginId(postLoginReq.getLoginId());

        // 아이디가 존재하지 않으면 새로운 CartMember 생성
        if (!authOptional.isPresent()) {
            CartMemberVO newCartMember = createNewCartMember(postLoginReq);
            return authenticateAndGenerateToken(newCartMember.getName(), postLoginReq.getPassword(), newCartMember.getCartMemberId());
        }

        // 아이디가 존재하나 비밀번호가 일치하지 않으면 예외 발생
        CartMemberVO auth = authOptional.get();
        if (!passwordEncoder.matches(postLoginReq.getPassword(), auth.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAIL_CART_MEMBER);
        }

        // 인증 후 토큰 생성
        return authenticateAndGenerateToken(auth.getName(), postLoginReq.getPassword(), auth.getCartMemberId());
    }

    /**
     * 새로운 CartMember 생성 메서드 (장바구니 멤버 회원가입)
     * @param postLoginReq 아이디와 비밀번호
     * @return 생성된 CartMemberVO 객체
     */
    private CartMemberVO createNewCartMember(PostLoginReq postLoginReq) {
        CartMemberVO cartMember = CartMemberVO.builder()
                .cartId(1L) // 임시로 1로 설정
                .name(postLoginReq.getLoginId())
                .password(passwordEncoder.encode(postLoginReq.getPassword()))
                .build();

        cartMapper.insertCartMember(cartMember);
        return cartMember;
    }

    /**
     * 인증 및 토큰 생성 메서드
     * @param username 사용자 이름
     * @param password 사용자 비밀번호
     * @param cartMemberId CartMember ID
     * @return JwtToken 객체
     */
    private JwtToken authenticateAndGenerateToken(String username, String password, Long cartMemberId) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        // 실제 검증
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성 후 반환
        return jwtTokenProvider.generateToken(authentication, cartMemberId);
    }
}
