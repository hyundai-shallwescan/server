package com.ite.sws.domain.product.service;

import com.ite.sws.domain.product.dto.GetProductDetailRes;
import com.ite.sws.domain.product.dto.GetProductReviewRes;
import com.ite.sws.domain.product.vo.ProductVO;
import java.util.List;

/**
 * 상품 서비스 인터페이스
 * @author 정은지
 * @since 2024.08.23
 * @version 1.0
 *
 * <pre>
 * 수정일        	수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.23  	정은지        최초 생성
 * 2024.08.27  	구지웅        기능 담당자 구지웅 작성
 * </pre>
 */
public interface ProductService {
    ProductVO findProduct(Long productId);
    GetProductDetailRes findProductDetail(Long productId);
    List<ProductVO> findProductsByProductName(String productName);
    List<GetProductReviewRes>findProductReviews(Long productId, int page, int size);
}
