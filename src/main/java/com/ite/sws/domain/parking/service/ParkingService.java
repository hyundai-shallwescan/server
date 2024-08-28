package com.ite.sws.domain.parking.service;

import com.ite.sws.domain.parking.dto.GetParkingRes;
import com.ite.sws.domain.parking.dto.PatchParkingReq;
import com.ite.sws.domain.parking.dto.PostParkingReq;

/**
 * 주차 서비스 인터페이스
 * @author 남진수
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	남진수       최초 생성
 * 2024.08.28  	남진수       주차 기록 추가 메서드 추가
 * 2024.08.28  	남진수       주차 기록 수정 메서드 추가
 * 2024.08.28  	남진수       주차 정산 정보 조회 메서드 추가
 * </pre>
 */
public interface ParkingService {

    /**
     * 주차 기록 추가(입차 처리)
     * @param postParkingReq 차량 번호
     */
    void addParkingHistory(PostParkingReq postParkingReq);

    /**
     * 주차 기록 수정(출차 처리)
     * @param patchParkingReq 차량 번호
     */
    void modifyParkingHistory(PatchParkingReq patchParkingReq);

    /**
     * 주차 정산 정보 조회
     * @param memberId 회원 ID
     * @return GetParkingRes
     */
    GetParkingRes findParkingInformation(Long memberId);
}
