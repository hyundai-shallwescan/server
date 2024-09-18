package com.ite.sws.domain.chat.service;

import com.ite.sws.domain.chat.dto.ChatDTO;
import com.ite.sws.domain.chat.dto.GetChatRes;
import com.ite.sws.domain.chat.mapper.ChatMapper;
import com.ite.sws.domain.chat.vo.ChatMessageVO;
import com.ite.sws.domain.fcm.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

/**
 * 채팅 서비스 구현체
 * @author 남진수
 * @since 2024.08.26
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	남진수       최초 생성
 * 2024.08.26  	남진수       채팅 메시지 조회 기능 추가
 * 2024.09.03  	남진수       채팅 메시지 저장 기능 추가
 * 2024.09.10  	남진수       푸쉬 알림 전송 기능 추가
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMapper chatMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final FcmService fcmService;

    /**
     * 채팅 메시지 저장
     * @param message 채팅 메시지
     */
    public void saveMessage(ChatDTO message) throws IOException {
        ChatMessageVO updatedMessage = ChatMessageVO.builder()
                .cartMemberId(message.getCartMemberId())
                .cartId(message.getCartId())
                .name(message.getName())
                .payload(message.getPayload())
                .status(message.getStatus())
                .build();
        chatMapper.insertMessage(updatedMessage);
        sendPushNotifications(message.getCartId(), message);
    }

    /**
     * 채팅 메시지 조회
     * @param cartId 장바구니 식별자
     * @return 채팅 메시지 리스트
     */
    public List<GetChatRes> getMessages(Long cartId) {
        return chatMapper.selectMessagesByCartId(cartId);
    }

    /**
     * 푸쉬 알림 전송
     * @param cartId 장바구니 식별자
     * @param message 채팅 메시지
     * @throws IOException IOException
     */
    public void sendPushNotifications(Long cartId, ChatDTO message) throws IOException {

        List<Long> cartMemberIdList = chatMapper.selectCartMemberIdListByCartId(cartId);

        for (Long cartMemberId : cartMemberIdList) {
            // 메시지를 보낸 사람에게는 메시지를 보내지 않음
            if (cartMemberId.equals(message.getCartMemberId())) {
                continue;
            }
            String fcmTokenKey = "FCM_TOKEN:" + cartMemberId;
            String targetToken = redisTemplate.opsForValue().get(fcmTokenKey);

            if (targetToken != null) {
                // 메시지 상태에 따라 다른 메시지 전송
                switch (message.getStatus()) {
                    case "NORMAL":
                        fcmService.sendMessageTo(targetToken, message.getName(), message.getPayload());
                        break;
                    case "CART":
                        fcmService.sendMessageTo(targetToken, message.getName(), "장바구니에 변동사항이 있습니다.");
                        break;
                    case "CHECK":
                        fcmService.sendMessageTo(targetToken, message.getName(), "요청리스트에 변동사항이 있습니다.");
                        break;
                }
            }
        }
    }
}
