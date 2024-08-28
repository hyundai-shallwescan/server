package com.ite.sws.domain.checklist.service;

import com.ite.sws.domain.checklist.dto.GetShareCheckListRes;
import com.ite.sws.domain.checklist.dto.PostShareCheckListReq;
import com.ite.sws.domain.checklist.mapper.ShareCheckListMapper;
import com.ite.sws.domain.checklist.vo.ShareCheckListItemVO;
import com.ite.sws.exception.CustomException;
import com.ite.sws.exception.ErrorCode;
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
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class ShareCheckListServiceImpl implements ShareCheckListService {

    private final ShareCheckListMapper shareCheckListMapper;

    /**
     * cartId로 공유 체크리스트 아이템 조회
     * @param cartId 장바구니 ID
     * @return 공유 체크리스트 아이템 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public GetShareCheckListRes findShareCheckList(Long cartId) {
        // cartId가 유효한지 확인
        if (shareCheckListMapper.selectCountByCartId(cartId) == 0) {
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
        // cartId, productId가 유효한지 확인
        if (shareCheckListMapper.selectCountByCartId(postShareCheckListReq.getCartId()) == 0) {
            throw new CustomException(ErrorCode.CART_NOT_FOUND);
        }
        if (shareCheckListMapper.selectCountByProductId(postShareCheckListReq.getProductId()) == 0) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        ShareCheckListItemVO newItem = ShareCheckListItemVO.builder()
                .cartId(postShareCheckListReq.getCartId())
                .productId(postShareCheckListReq.getProductId())
                .build();
        shareCheckListMapper.insertShareCheckListItem(newItem);
    }

    /**
     * 공유 체크리스트 아이템 삭제
     * @param cartId 장바구니 PK
     * @param productId 상품 PK
     */
    @Override
    public void removeShareCheckListItem(Long cartId, Long productId) {
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
    }
}
