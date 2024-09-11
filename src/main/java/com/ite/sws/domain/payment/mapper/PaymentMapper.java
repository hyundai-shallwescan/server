package com.ite.sws.domain.payment.mapper;

import com.ite.sws.domain.payment.vo.CartQRCodeVO;
import com.ite.sws.domain.payment.vo.PaymentItemVO;
import com.ite.sws.domain.payment.vo.PaymentVO;
import com.ite.sws.domain.product.vo.ProductVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 상품 결제 매퍼 인터페이스
 * @author 김민정
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	김민정       최초 생성
 * 2024.08.28  	김민정       상품 결제 정보 삽입을 위한 프로시저 호출
 * 2024.08.28  	김민정       새로운 장바구니 및 인증 QR 정보 삽입을 위한 프로시저 호출
 * 2024.08.28  	김민정       결제 ID를 통한 출입증 상태 사용 처리
 * 2024.08.30  	김민정       멤버의 이전 구매 기록에서 비슷한 가격대의 제품을 찾기
 * 2024.08.30  	김민정       전체 상품 중에서 비슷한 가격대의 제품을 찾기
 * 2024.09.01   김민정       결제 후, 새로운 장바구니 ID 반환 기능 추가
 * 2024.09.10   김민정       결제 아이템 삽입
 * </pre>
 */
public interface PaymentMapper {

    /**
     * 상품 결제 정보 삽입을 위한 프로시저 호출
     * @param newPayment 상품 결제 생성 객체
     */
    void insertPayment(PaymentVO newPayment);

    /**
     * 결제 아이템 삽입
     * @param paymentId 생성한 결제 ID
     * @param items 결제 아이템 목록
     */
    void insertPaymentItems(@Param("paymentId") Long paymentId, @Param("items") List<PaymentItemVO> items);

    /**
     * 새로운 장바구니 및 인증 QR 정보 삽입을 위한 프로시저 호출
     * @param cartQRCodeVO 장바구니 및 QR 생성 객체
     */
    Long insertCartAndQRCode(CartQRCodeVO cartQRCodeVO);

    /**
     * 결제 ID를 통한 출입증 상태 사용 처리
     * @param paymentId 결제 ID
     */
    int updateExitCredential(Long paymentId);

    /**
     * 멤버의 이전 구매 기록에서 비슷한 가격대의 제품을 찾기
     * @param memberId
     * @param targetAmount
     * @return
     */
    ProductVO findPreviousPurchasesInPriceRange(@Param("memberId") Long memberId,
                                                @Param("targetAmount") Long targetAmount);

    /**
     * 전체 상품 중에서 비슷한 가격대의 제품을 찾기
     * @param targetAmount
     * @return
     */
    ProductVO findSingleProductInPriceRange(@Param("targetAmount") Long targetAmount);
}
