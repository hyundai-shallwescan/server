package com.ite.sws.domain.cart.mapper;

import com.ite.sws.domain.cart.dto.CartItemChatDTO;
import com.ite.sws.domain.cart.dto.CartItemDTO;
import com.ite.sws.domain.cart.dto.CartItemMessageDTO;
import com.ite.sws.domain.cart.dto.CartTotalDTO;
import com.ite.sws.domain.cart.vo.CartItemVO;
import com.ite.sws.domain.cart.vo.CartMemberVO;
import com.ite.sws.domain.cart.vo.CartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 장바구니 매퍼 인터페이스
 * @author 김민정
 * @since 2024.08.26
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	김민정       최초 생성
 * 2024.08.26  	김민정       ACTIVE 상태인 가장 최신의 Cart ID를 가져오기 기능 추가
 * 2024.08.26  	김민정       Cart ID에 해당하는 장바구니 아이템 가져오기 기능 추가
 * 2024.08.26  	김민정       새로운 장바구니 생성
 * 2024.08.26  	남진수       장바구니 유저 생성
 * 2024.08.26  	남진수       로그인 아이디로 장바구니 유저 조회
 * 2024.08.26  	김민정       새로운 장바구니 생성 기능 추가
 * 2024.08.26  	김민정       장바구니 아이템 추가 및 수량 증가 기능 추가
 * 2024.08.26  	김민정       바코드로 상품 조회
 * 2024.08.26  	김민정       장바구니 아이템 수량 업데이트를 위한 프로시저 호출
 * 2024.08.26  	김민정       장바구니 아이템 삭제
 * 2024.08.26  	김민정       장바구니가 존재하는지 확인
 * 2024.08.26  	김민정       상품이 존재하는지 확인
 * 2024.08.30  	김민정       장바구니에 해당하는 아이템들의 총 금액과 MEMBER_ID 조회
 * 2024.09.05  	김민정       장바구니 아이템 정보 조회
 * 2024.09.06   남진수       memberId로 cartMemberId 조회 기능 추가
 * 2024.09.08   김민정       cartMemberId로 장바구니 유저 이름 조회
 * 2024.09.08   김민정       productId로 채팅용 상품 정보 조회
 * 2024.09.08   김민정       cartId로 memberId 조회
 * </pre>
 */
public interface CartMapper {

    /**
     * ACTIVE 상태인 가장 최신의 Cart ID를 가져오기
     * @param memberId 멤버 ID
     * @return 장바구니 ID
     */
    Long selectActiveCartByMemberId(@Param("memberId") Long memberId);

    /**
     * Cart ID에 해당하는 장바구니 아이템 가져오기
     * @param cartId 장바구니 ID
     * @return 장바구니 아이템 리스트
     */
    List<CartItemDTO> selectCartItemListByCartId(@Param("cartId") Long cartId);

    /**
     * 새로운 장바구니 생성
     * @param cart 장바구니 객체
     */
    void insertCart(CartVO cart);

    /**
     * 장바구니 유저 생성
     * @param cartMember
     */
    void insertCartMember(CartMemberVO cartMember);

    /**
     * 로그인 아이디로 장바구니 유저 조회
     * @param loginId
     * @return 장바구니 유저
     */
    Optional<CartMemberVO> selectCartMemberByLoginId(String loginId);
  
    /**
     * 장바구니 아이템 추가 및 수량 증가
     * @param cartItem 장바구니 아이템 객체
     */
    void insertCartItem(CartItemVO cartItem);

    /**
     * 바코드로 상품 조회
     * @param barcode 바코드 번호
     * @return
     */
    Long selectProductByBarcode(String barcode);

    /**
     * 장바구니 아이템 수량 업데이트를 위한 프로시저 호출
     * @param modifyCartItem 장바구니 아이템 수정 객체
     */
    void updateCartItemQuantity(CartItemVO modifyCartItem);

    /**
     * 장바구니 아이템 삭제
     * @param deleteCartItem 장바구니 아이템 삭제 객체
     */
    void deleteCartItem(CartItemVO deleteCartItem);

    /**
     * 장바구니가 존재하는지 확인
     * @param cartId 장바구니 ID
     * @return
     */
    int selectCountByCartId(@Param("cartId") Long cartId);

    /**
     * 장바구니에 해당하는 아이템들의 총 금액과 MEMBER_ID 조회
     * @param cartId 장바구니 ID
     * @return
     */
    CartTotalDTO calculateTotalCartValue(@Param("cartId") Long cartId);

    /**
     * 장바구니 아이템 정보 조회
     * @param cartId 장바구니 ID
     * @param productId 상품 ID
     * @return
     */
    CartItemMessageDTO selectCartItemDetail(@Param("cartId") Long cartId,
                                            @Param("productId") Long productId);

    /**
     * memberId로 cartMemberId 조회
     * @param memberId 멤버 id
     * @return cartId
     */
    Long selectCartMemberIdByMemberId(Long memberId);

    /**
     * cartMemberId로 장바구니 유저 이름 조회
     * @param cartMemberId 장바구니 멤버 ID
     * @return
     */
    String selectNameByCartMemberId(@Param("cartMemberId") Long cartMemberId);

    /**
     * productId로 채팅용 상품 정보 조회
     * @param productId 상품 ID
     */
    CartItemChatDTO selectCartItemChatDetails(@Param("productId") Long productId,
                                              @Param("cartMemberId") Long cartMemberId);

    /**
     * cartId로 memberId 조회
     * @param cartId 장바구니 ID
     * @return
     */
    Long selectMemberIdByCartId(@Param("cartId") Long cartId);

    String selectMemberNameByCartId(@Param("cartId") Long cartId);

}

