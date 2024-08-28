package com.ite.sws.domain.parking.controller;

import com.ite.sws.domain.parking.dto.PostParkingReq;
import com.ite.sws.domain.parking.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 주차 컨트롤러
 * @author 남진수
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	남진수       최초 생성
 * 2024.08.28  	남진수       주차 기록 추가 API 생성
 * </pre>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/parkings")
public class ParkingController {

    private final ParkingService parkingService;

    /**
     * 주차 기록 추가(입차 처리)
     * @param postParkingReq 차량 번호
     * @return ResponseEntity<Void>
     */
    @PostMapping
    public ResponseEntity<Void> addParkingHistory(@RequestBody PostParkingReq postParkingReq) {
        parkingService.addParkingHistory(postParkingReq);
        return ResponseEntity.ok().build();
    }
}
