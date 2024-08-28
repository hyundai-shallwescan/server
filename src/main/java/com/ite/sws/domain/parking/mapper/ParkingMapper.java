package com.ite.sws.domain.parking.mapper;

import com.ite.sws.domain.parking.vo.ParkingHistoryVO;

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
}
