package com.ite.sws.domain.cart.service;

import com.ite.sws.domain.cart.dto.CartItemDTO;
import com.ite.sws.domain.cart.dto.CartItemMessageDTO;
import com.ite.sws.domain.cart.dto.GetCartRes;
import com.ite.sws.domain.cart.dto.PostCartItemReq;
import com.ite.sws.domain.cart.dto.PostCartLoginReq;
import com.ite.sws.domain.cart.event.CartUpdateEvent;
import com.ite.sws.domain.cart.mapper.CartMapper;
import com.ite.sws.domain.cart.vo.CartItemVO;
import com.ite.sws.domain.cart.vo.CartMemberVO;
import com.ite.sws.domain.cart.vo.CartVO;
import com.ite.sws.domain.member.dto.JwtToken;
import com.ite.sws.domain.product.mapper.ProductMapper;
import com.ite.sws.util.JwtTokenProvider;
import com.ite.sws.exception.CustomException;
import com.ite.sws.exception.ErrorCode;
import com.ite.sws.util.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
 * 2024.08.26  	김민정       cartId로 장바구니 아이템 조회 기능 추가
 * 2024.08.26   김민정       새로운 장바구니 생성 기능 추가
 * 2024.08.26   남진수       장바구니 로그인 기능 추가
 * 2024.08.27   남진수       CartMember 생성 메서드 추가 (장바구니 회원가입)
 * 2024.08.27   남진수       인증 및 토큰 생성 메서드 추가
 * 2024.08.26   김민정       MemberId로 장바구니 조회 기능 추가
 * 2024.08.26  	김민정       장바구니 아이템 추가 및 수량 증가 기능 추가
 * 2024.08.26  	김민정       장바구니 아이템 수량 변경 기능 추가
 * 2024.08.26  	김민정       장바구니 아이템 삭제
 * 2024.08.31  	김민정       장바구니 아이템 조회 시, 장바구니 총 금액 계산
 * 2024.09.05   김민정       장바구니 상태 변화 웹소켓으로 전송
 * 2024.09.06   남진수       memberId로 cartMemberId 조회 기능 추가
 * 2024.09.07   김민정       장바구니 변경 사항을 알리는 이벤트 발행 (비동기 처리)
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final ProductMapper productMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final MessageSender messageSender;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * cartId로 장바구니 아이템 조회
     * @param cartId 멤버 식별자
     * @return 장바구니 아이템 리스트
     */
    @Override
    @Transactional
    public GetCartRes findCartItemList(Long cartId) {
        // 해당 cart_id에 속하는 cart items 가져오기
        List<CartItemDTO> cartItems = cartMapper.selectCartItemListByCartId(cartId);

        return GetCartRes.builder()
                .cartId(cartId)
                .totalPrice(calculateCartTotalPrice(cartItems))
                .items(cartItems)
                .build();
    }

    /**
     * 장바구니 총 금액 계산
     * @param cartItems
     * @return
     */
    private Long calculateCartTotalPrice(List<CartItemDTO> cartItems) {
        return cartItems.stream()
                .mapToLong(item -> item.getProductPrice() * item.getQuantity())
                .sum();
    }
  
    /**
     * 새로운 장바구니 생성
     * @param memberId 멤버 식별자
     * @return 새로 생성된 장바구니 ID
     */
    private Long addNewCart(Long memberId) {
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
     * @param postCartLoginReq 아이디, 비밀번호
     * @return JwtToken 객체
     */
    @Transactional
    public JwtToken cartLogin(PostCartLoginReq postCartLoginReq) {
        Optional<CartMemberVO> authOptional = cartMapper.selectCartMemberByLoginId(postCartLoginReq.getLoginId());

        // 아이디가 존재하지 않으면 새로운 CartMember 생성
        if (!authOptional.isPresent()) {
            CartMemberVO newCartMember = createNewCartMember(postCartLoginReq);
            return authenticateAndGenerateToken(newCartMember.getName(), postCartLoginReq.getPassword(), newCartMember.getCartMemberId());
        }

        // 아이디가 존재하나 비밀번호가 일치하지 않으면 예외 발생
        CartMemberVO auth = authOptional.get();
        if (!passwordEncoder.matches(postCartLoginReq.getPassword(), auth.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAIL);
        }

        // 인증 후 토큰 생성
        return authenticateAndGenerateToken(auth.getName(), postCartLoginReq.getPassword(), auth.getCartMemberId());
    }

    /**
     * 새로운 CartMember 생성 메서드 (장바구니 멤버 회원가입)
     * @param postCartLoginReq 아이디와 비밀번호
     * @return 생성된 CartMemberVO 객체
     */
    private CartMemberVO createNewCartMember(PostCartLoginReq postCartLoginReq) {
        CartMemberVO cartMember = CartMemberVO.builder()
                .name(postCartLoginReq.getLoginId())
                .password(passwordEncoder.encode(postCartLoginReq.getPassword()))
                .cartId(postCartLoginReq.getCartId())
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
    @Transactional
    public JwtToken authenticateAndGenerateToken(String username, String password, Long cartMemberId) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        // 실제 검증
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 인증 정보를 기반으로 JWT 토큰 생성 후 반환
        return jwtTokenProvider.generateToken(authentication, null, cartMemberId);
    }

    /**
     * 장바구니 아이템 추가 및 수량 증가
     * (1) 장바구니에 해당 상품이 없을 경우 새로운 아이템을 생성
     * (2) 장바구니에 해당 상품이 이미 존재할 경우 수량을 증가
     *
     * @param postCartItemReq 장바구니 아이템 객체
     * @param memberId 회원 ID
     */
    @Override
    @Transactional
    public void addAndModifyCartItem(PostCartItemReq postCartItemReq, Long memberId) {
        Long cartId = findCartByMemberId(memberId);   // 유저의 장바구니 조회
        Long productId = cartMapper.selectProductByBarcode(postCartItemReq.getBarcode());   // 바코드 번호로 상품 아이디 조회
        if (productId == null) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        // 장바구니 아이템 생성 시 필요한 데이터 설정
        CartItemVO vo = CartItemVO.builder()
                .cartId(cartId)
                .productId(productId)
                .build();
        cartMapper.insertCartItem(vo);  // 장바구니 아이템 추가 또는 수량 증가

        // 장바구니 변경 사항을 알리는 이벤트 발행 (비동기 처리)
        CartItemMessageDTO messageDTO = cartMapper.selectCartItemDetail(cartId, productId);
        messageDTO.setAction(messageDTO.getQuantity() == 1 ? "create" : "increase"); // 아이템 수량이 1이면 새로운 생성, 1 초과면 증가
        eventPublisher.publishEvent(new CartUpdateEvent(this, messageDTO));
    }

    /**
     * MemberId로 장바구니 조회
     * @param memberId 멤버 식별자
     * @return 멤버의 장바구니 식별자
     */
    @Transactional
    @Override
    public Long findCartByMemberId(Long memberId) {
        // 장바구니에서 ACTIVE인 상태 중 가장 최근에 생성된 cart 가져오기
        Long cartId = cartMapper.selectActiveCartByMemberId(memberId);

        // 해당 유저의 장바구니가 없을 시, 장바구니 생성
        if (cartId == null) {
            cartId = addNewCart(memberId);
        }

        return cartId;
    }

    /**
     * 장바구니 아이템 수량 변경
     * @param cartId 장바구니 ID
     * @param productId 상품 ID
     * @param delta 수량 변화량 (+1, -1)
     */
    @Transactional
    @Override
    public void modifyCartItemQuantity(Long cartId, Long productId, int delta) {
        // cartId가 유효한지 확인
        if (cartMapper.selectCountByCartId(cartId) == 0) {
            throw new CustomException(ErrorCode.CART_NOT_FOUND);
        }

        // productId가 유효한지 확인
        if (productMapper.selectCountByProductId(productId) == 0) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        CartItemVO modifyCartItem = CartItemVO.builder()
                .cartId(cartId)
                .productId(productId)
                .quantity(delta)
                .build();
        cartMapper.updateCartItemQuantity(modifyCartItem);

        // 장바구니 변경 사항을 알리는 이벤트 발행 (비동기 처리)
        String action = delta > 0 ? "increase" : "decrease";
        CartItemMessageDTO messageDTO = CartItemMessageDTO.builder()
                .cartId(cartId)
                .productId(productId)
                .action(action)
                .build();
        eventPublisher.publishEvent(new CartUpdateEvent(this, messageDTO));
    }

    /**
     * 장바구니 아이템 삭제
     * @param cartId 장바구니 ID
     * @param productId 상품 ID
     */
    @Transactional
    @Override
    public void removeCartItem(Long cartId, Long productId) {
        // cartId가 유효한지 확인
        if (cartMapper.selectCountByCartId(cartId) == 0) {
            throw new CustomException(ErrorCode.CART_NOT_FOUND);
        }

        // productId가 유효한지 확인
        if (productMapper.selectCountByProductId(productId) == 0) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        CartItemVO deleteCartItem = CartItemVO.builder()
                .cartId(cartId)
                .productId(productId)
                .build();
        cartMapper.deleteCartItem(deleteCartItem);

        // 장바구니 변경 사항을 알리는 이벤트 발행 (비동기 처리)
        CartItemMessageDTO messageDTO = CartItemMessageDTO.builder()
                .cartId(cartId)
                .productId(productId)
                .action("delete")
                .build();
        eventPublisher.publishEvent(new CartUpdateEvent(this, messageDTO));
    }


    /**
     * MemberId로 cartMemberId 조회
     * @param memberId 멤버 id
     * @return cartMemberId
     */
    public Long findCartMemberIdByMemberId(Long memberId) {
        // 회원의 CART 중 'ACTIVE' 상태인 cartMemberId 가져오기
        return cartMapper.selectCartMemberIdByMemberId(memberId);
    }

}
