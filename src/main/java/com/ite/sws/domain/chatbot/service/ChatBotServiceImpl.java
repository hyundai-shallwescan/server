package com.ite.sws.domain.chatbot.service;

import com.ite.sws.domain.chatbot.dto.GetChatGptRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 챗봇 서비스 구현체
 * @author 정은지
 * @since 2024.09.08
 * @version 1.0
 *
 * <pre>
 * 수정일        	수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.09.08  	정은지        최초 생성
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class ChatBotServiceImpl implements ChatBotService {

    private final RestTemplate restTemplate;

    @Value("${flask.url}")
    private String flaskBaseUrl;

    /**
     * 사용자 질의에 대한 챗봇 응답 반환
     * @param query 사용자 질의
     * @return GetChatGptRes 챗봇 응답 객체
     */
    @Override
    public GetChatGptRes getResponse(String query) {
        String flaskUrl = flaskBaseUrl + "/match?query=" + query;

        ResponseEntity<GetChatGptRes> response = restTemplate.postForEntity(flaskUrl, null, GetChatGptRes.class);
        GetChatGptRes responseBody = response.getBody();

        // product가 없는 경우, message만 반환
        if (responseBody.getProduct() == null || responseBody.getProduct().isEmpty()) {
            return GetChatGptRes.builder()
                    .message(responseBody != null ? responseBody.getMessage() : "No products available.")
                    .build();
        }

        // product가 있을 경우
        List<GetChatGptRes.Product> productList = responseBody.getProduct().stream()
                .map(product -> GetChatGptRes.Product.builder()
                        .id(product.getId())
                        .title(product.getTitle())
                        .price(product.getPrice())
                        .thumbnailImage(product.getThumbnailImage())
                        .build()
                ).collect(Collectors.toList());

        return GetChatGptRes.builder()
                .message(responseBody.getMessage())
                .product(productList)
                .build();
    }
}