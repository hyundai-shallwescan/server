package com.ite.sws.domain.checklist.service;

import com.ite.sws.domain.checklist.dto.GetShareCheckListRes;
import com.ite.sws.domain.checklist.dto.PostShareCheckListReq;
import com.ite.sws.domain.checklist.mapper.ShareCheckListMapper;
import com.ite.sws.domain.checklist.vo.ShareCheckListItemVO;
import com.ite.sws.exception.CustomException;
import com.ite.sws.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

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
        List<ShareCheckListItemVO> items = shareCheckListMapper.selectShareCheckListByCartId(cartId);
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
}
