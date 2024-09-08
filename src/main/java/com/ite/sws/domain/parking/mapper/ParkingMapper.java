package com.ite.sws.domain.parking.mapper;

import com.ite.sws.domain.parking.dto.CartItemListDTO;
import com.ite.sws.domain.parking.dto.ParkingHistoryDTO;
import com.ite.sws.domain.parking.dto.ParkingPaymentDTO;
import com.ite.sws.domain.parking.vo.ParkingHistoryVO;
import java.util.List;

/**
 * 주차 Mapper 인터페이스
 * @author 남진수
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	남진수       최초 생성
 * 2024.08.28  	남진수       차량 번호로 회원 ID 조회 메서드 추가
 * 2024.08.28  	남진수       주차 기록 추가 메서드 추가
 * 2024.08.28  	남진수       주차 기록 수정 메서드 추가
 * 2024.08.28  	남진수       주차 기록 조회 메서드 추가
 * 2024.08.28  	남진수       결제 금액 조회 메서드 추가
 * 2024.08.28  	남진수       장바구니 금액 조회 메서드 추가
 * </pre>
 */
public interface ParkingMapper {

    /**
     * 차량 번호로 회원 ID 조회
     * @param carNumber 차량 번호
     * @return 회원 ID
     */
    Long selectMemberIdByCarNumber(String carNumber);

    /**
     * 주차 기록 추가(입차 처리)
     * @param parkingHistoryVO 주차 기록 VO
     */
    void insertParkingHistory(ParkingHistoryVO parkingHistoryVO);

    /**
     * 주차 기록 수정(출차 처리)
     * @param parkingHistoryVO 주차 기록 VO
     */
    void updateParkingHistory(ParkingHistoryVO parkingHistoryVO);

    /**
     * 주차 기록 조회
     * @param memberId 회원 ID
     * @return 주차 기록 DTO (입차 시간, 차량 번호)
     */
    ParkingHistoryDTO selectParkingHistoryByMemberId(Long memberId);

    /**
     * 결제 관련 정보 조회
     * @param memberId 회원 ID
     * @return 결제 ID, 결제 금액
     */
    ParkingPaymentDTO selectPaymentIdAndAmountByMemberId(Long memberId);

    /**
     * 장바구니금액 정보 조회
     * @param memberId 회원 ID
     * @return 장바구니 상품 목록(가격, 수량) List
     */
    List<CartItemListDTO> selectCartItemListByMemberId(Long memberId);

    /**
     * 주차 결제 상태 조회
     * @param parkingPaymentId 주차 결제 ID
     * @return 주차 결제 상태
     */
    String selectParkingPaymentStatusByPaymentId(Long parkingPaymentId);
}
