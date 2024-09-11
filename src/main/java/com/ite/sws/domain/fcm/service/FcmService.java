package com.ite.sws.domain.fcm.service;

import java.io.IOException;

/**
 * FCM 메시지 DTO
 * @author 남진수
 * @since 2024.09.10
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.10  	남진수       최초 생성
 * 2024.09.10  	남진수       FCM 메시지 전송 기능 추가
 * </pre>
 */
public interface FcmService {

    /**
     * FCM 메시지 전송
     * @param targetToken 대상 FCM 토큰
     * @param title 보낸 사람
     * @param body 내용
     * @throws IOException
     */
    void sendMessageTo(String targetToken, String title, String body) throws IOException;

}
