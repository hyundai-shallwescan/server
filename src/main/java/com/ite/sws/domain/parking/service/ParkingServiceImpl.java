package com.ite.sws.domain.parking.service;

import com.ite.sws.domain.parking.dto.PostParkingReq;
import com.ite.sws.domain.parking.mapper.ParkingMapper;
import com.ite.sws.domain.parking.vo.ParkingHistoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 주차 서비스 구현체
 * @author 남진수
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	남진수       최초 생성
 * 2024.08.28  	남진수       주차 기록 추가 메서드 추가
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService{

    private final ParkingMapper parkingMapper;

    /**
     * 주차 기록 추가(입차 처리)
     * @param postParkingReq 차량 번호
     */
    @Transactional
    public void addParkingHistory(PostParkingReq postParkingReq){
        Long memberId = parkingMapper.selectMemberIdByCarNumber(postParkingReq.getCarNumber());
        LocalDateTime kstLocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        ParkingHistoryVO parkingHistoryVO = ParkingHistoryVO.builder()
                .memberId(memberId)
                .carNumber(postParkingReq.getCarNumber())
                .entranceAt(kstLocalDateTime)
                .build();
        parkingMapper.insertParkingHistory(parkingHistoryVO);
    }

}
