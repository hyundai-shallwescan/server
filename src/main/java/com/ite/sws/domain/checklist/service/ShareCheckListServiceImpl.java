package com.ite.sws.domain.checklist.service;

import com.ite.sws.domain.cart.mapper.CartMapper;
import com.ite.sws.domain.chat.dto.ChatDTO;
import com.ite.sws.domain.chat.mapper.ChatMapper;
import com.ite.sws.domain.chat.vo.ChatMessageVO;
import com.ite.sws.domain.checklist.dto.GetShareCheckListRes;
import com.ite.sws.domain.checklist.dto.PostShareCheckListReq;
import com.ite.sws.domain.checklist.dto.ShareCheckMessageDTO;
import com.ite.sws.domain.checklist.event.ShareCheckListEventPublisher;
import com.ite.sws.domain.checklist.mapper.ShareCheckListMapper;
import com.ite.sws.domain.checklist.vo.ShareCheckListItemVO;
import com.ite.sws.exception.CustomException;
import com.ite.sws.exception.ErrorCode;
import com.ite.sws.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ite.sws.exception.ErrorCode.DATABASE_ERROR;
import static com.ite.sws.exception.ErrorCode.SHARE_CHECK_LIST_ITEM_NOT_FOUND;

/**
 * 공유 체크리스트 서비스 구현체
 * @author 김민정
 * @since 2024.08.27
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.27  	김민정       최초 생성
 * 2024.08.27  	김민정       cartId로 공유 체크리스트 아이템 조회 기능 추가
 * 2024.08.27  	김민정       공유 체크리스트에 아이템 생성 기능 추가
 * 2024.08.28  	김민정       공유 체크리스트 아이템 삭제 기능 추가
 * 2024.08.28  	김민정       공유 체크리스트 아이템 체크 상태 변경 기능 추가
 * 2024.09.12   김민정       공유 체크리스트 변경 실시간 처리
 * 2024.09.12   김민정       공유 체크리스트 변경 채팅 발송
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class ShareCheckListServiceImpl implements ShareCheckListService {

    private final ShareCheckListMapper shareCheckListMapper;
    private final CartMapper cartMapper;
    private final ChatMapper chatMapper;
    private final ShareCheckListEventPublisher eventPublisher;

    /**
     * cartId로 공유 체크리스트 아이템 조회
     * @param cartId 장바구니 ID
     * @return 공유 체크리스트 아이템 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public GetShareCheckListRes findShareCheckList(Long cartId) {
        // cartId가 유효한지 확인
        if (cartMapper.selectCountByCartId(cartId) == 0) {
            throw new CustomException(ErrorCode.CART_NOT_FOUND);
        }
        List<GetShareCheckListRes.GetShareCheckRes> items = shareCheckListMapper.selectShareCheckListByCartId(cartId);
        return GetShareCheckListRes.builder()
                .cartId(cartId)
                .items(items)
                .build();
    }

    /**
     * 공유 체크리스트에 아이템 생성
     * @param postShareCheckListReq 공유 체크리스트 아이템 객체
     */
    @Override
    @Transactional
    public void addShareCheckListItem(PostShareCheckListReq postShareCheckListReq) {
        Long cartId = postShareCheckListReq.getCartId();
        Long productId = postShareCheckListReq.getProductId();
        ShareCheckListItemVO newItem = ShareCheckListItemVO.builder()
                .cartId(cartId)
                .productId(productId)
                .build();
        shareCheckListMapper.insertShareCheckListItem(newItem);

        if (newItem.getRowCount()==0) {
            throw new CustomException(ErrorCode.SHARE_CHECK_LIST_ITEM_ALREADY_EXISTS);
        }

        // 공유 체트리스트 변경 관련 이벤트 발행 (비동기 처리)
        // (1) 공유 체크리스트 변경 실시간 처리
        ShareCheckMessageDTO messageDTO = shareCheckListMapper.selectShareCheck(cartId, productId);
        messageDTO.setAction("create");
        eventPublisher.publishShareCheckListEvent(messageDTO);

        // (2) 공유 체크리스트 변경 채팅 발송
        ChatDTO chatDTO = toChatDTO(messageDTO);
        chatMapper.insertMessage(toChatMessageVO(chatDTO));     // 메시지 DB 저장
        eventPublisher.publishShareCheckListChatEvent(chatDTO); // 웹 소켓 전송
    }

    /**
     * 공유 체크리스트 아이템 삭제
     * @param cartId 장바구니 PK
     * @param productId 상품 PK
     */
    @Override
    @Transactional
    public void removeShareCheckListItem(Long cartId, Long productId) {
        // 웹소켓용 체크리스트 아이템 상세 DTO
        ShareCheckMessageDTO messageDTO = shareCheckListMapper.selectShareCheck(cartId, productId);

        // 삭제할 체크리스트 아이템
        ShareCheckListItemVO deleteItem = ShareCheckListItemVO.builder()
                .cartId(cartId)
                .productId(productId)
                .build();

        try {
            shareCheckListMapper.deleteShareCheckListItem(deleteItem);
        } catch (UncategorizedSQLException e) {
            if (e.getSQLException().getErrorCode() == 20001) {
                throw new CustomException(SHARE_CHECK_LIST_ITEM_NOT_FOUND);
            }
            // 다른 예외 처리
            throw new CustomException(DATABASE_ERROR);
        }

        // 공유 체트리스트 변경 관련 이벤트 발행 (비동기 처리)
        // (1) 공유 체크리스트 변경 실시간 처리
        messageDTO.setAction("delete");
        eventPublisher.publishShareCheckListEvent(messageDTO);

        // (2) 공유 체크리스트 변경 채팅 발송
        ChatDTO chatDTO = toChatDTO(messageDTO);
        chatMapper.insertMessage(toChatMessageVO(chatDTO));     // 메시지 DB 저장
        eventPublisher.publishShareCheckListChatEvent(chatDTO); // 웹 소켓 전송
    }

    /**
     * 공유 체크리스트 아이템 체크 상태 변경
     * @param cartId 장바구니 PK
     * @param productId 상품 PK
     */
    @Override
    @Transactional
    public void modifyShareCheckListItem(Long cartId, Long productId) {
        try {
            shareCheckListMapper.updateShareCheckListItem(cartId, productId);
        } catch (UncategorizedSQLException e) {
            if (e.getSQLException().getErrorCode() == 20001) {
                throw new CustomException(SHARE_CHECK_LIST_ITEM_NOT_FOUND);
            }
            // 다른 예외 처리
            throw new CustomException(DATABASE_ERROR);
        }

        // 공유 체트리스트 변경 관련 이벤트 발행 (비동기 처리)
        // (1) 공유 체크리스트 변경 실시간 처리
        ShareCheckMessageDTO messageDTO = shareCheckListMapper.selectShareCheck(cartId, productId);
        messageDTO.setAction("update");
        eventPublisher.publishShareCheckListEvent(messageDTO);

        // (2) 공유 체크리스트 변경 채팅 발송
        ChatDTO chatDTO = toChatDTO(messageDTO);
        chatMapper.insertMessage(toChatMessageVO(chatDTO));     // 메시지 DB 저장
        eventPublisher.publishShareCheckListChatEvent(chatDTO); // 웹 소켓 전송
    }

    /**
     * ShareCheckMessageDTO -> ChatDTO 변환
     * @param messageDTO 채팅 객체
     * @return
     */
    private ChatDTO toChatDTO(ShareCheckMessageDTO messageDTO) {
        Long cartMemberId = SecurityUtil.getCurrentCartMemberId();
        String name = cartMapper.selectNameByCartMemberId(cartMemberId);

        return ChatDTO.builder()
                .cartMemberId(cartMemberId)
                .cartId(messageDTO.getCartId())
                .name(name)
                .payload(toJsonPayload(messageDTO)) // JSON 변환 메서드
                .status("CHECK")
                .build();
    }

    /**
     * JSON to String 변환 메서드
     * @param dto 메시지 객체
     * @return
     */
    private String toJsonPayload(ShareCheckMessageDTO dto) {
        return "{ \"action\": \"" + dto.getAction() + "\", "
                + "\"productName\": \"" + dto.getProductName() + "\", "
                + "\"productThumbnail\": \"" + dto.getProductThumbnail() + "\" }";
    }

    /**
     * ChatDTO -> ChatMessageVO 변환
     */
    private ChatMessageVO toChatMessageVO(ChatDTO message) {
        return ChatMessageVO.builder()
                .cartMemberId(message.getCartMemberId())
                .cartId(message.getCartId())
                .name(message.getName())
                .payload(message.getPayload())
                .status(message.getStatus())
                .build();
    }
}
