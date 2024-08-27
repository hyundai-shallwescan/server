package com.ite.sws.domain.product.service;

import com.ite.sws.domain.product.dto.GetProductDetailRes;
import com.ite.sws.domain.product.mapper.ProductMapper;
import com.ite.sws.domain.product.vo.ProductVO;
import com.ite.sws.exception.CustomException;
import com.ite.sws.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

/**
 * 상품 서비스 구현체
 * @author 정은지
 * @since 2024.08.23
 * @version 1.0
 *
 * <pre>
 * 수정일        	수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.23  	정은지        최초 생성
 * </pre>
 */

@Log4j
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;

    @Override
    public ProductVO findProduct(Long productId) {
        return mapper.selectProduct(productId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.PRODUCT_IS_NOT_FOUND);
        });
    }

    @Override
    public GetProductDetailRes findProductDetail(Long productId) {
        return mapper.selectProductDetail(productId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.PRODUCT_IS_NOT_FOUND);
        });
    }
}
