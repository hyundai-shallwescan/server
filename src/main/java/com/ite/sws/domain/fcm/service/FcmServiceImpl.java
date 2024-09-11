package com.ite.sws.domain.fcm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.ite.sws.domain.fcm.dto.FcmMessage;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Collections;

/**
 * FCM 메시지 서비스 구현체
 * @author 남진수
 * @since 2024.09.10
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.10  	남진수       최초 생성
 * 2024.09.10  	남진수       FCM 메시지 전송 기능 추가
 * 2024.09.10  	남진수       FCM 메시지 구성 기능 추가
 * 2024.09.10  	남진수       FCM 메시지 전송을 위한 토큰 발급 기능 추가
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class FcmServiceImpl implements FcmService {

    @Value("${fcm.api.url}")
    private String API_URL;
    private final ObjectMapper objectMapper;

    /**
     * FCM 메시지 전송
     * @param targetToken 대상 FCM 토큰
     * @param title 보낸 사람
     * @param body 내용
     * @throws IOException IO 예외
     */
    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
                MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            assert response.body() != null;
            System.out.println("FCM 메시지 전송 성공: " + response.body().string());
        } else {
            System.err.println("FCM 메시지 전송 실패: " + response.code() + ", " + response.message());
        }
    }

    /**
     * FCM 메시지 구성
     * @param targetToken 대상 FCM 토큰
     * @param title 보낸 사람
     * @param body 내용
     * @return FCM 메시지
     * @throws com.fasterxml.jackson.core.JsonProcessingException JSON 처리 예외
     */
    private String makeMessage(String targetToken, String title, String body) throws com.fasterxml.jackson.core.JsonProcessingException  {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();
        return objectMapper.writeValueAsString(fcmMessage);
    }

    /**
     * FCM 메시지 전송을 위한 토큰 발급
     * @return Bearer 토큰
     */
    private String getAccessToken() {
        final String firebaseConfigPath = "com/ite/sws/firebase/firebase_service_key.json";

        try {
            final GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                    .createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));

            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();

        } catch (IOException e) {
            throw new RuntimeException("Google access token 얻어오기 실패", e);
        }
    }
}
