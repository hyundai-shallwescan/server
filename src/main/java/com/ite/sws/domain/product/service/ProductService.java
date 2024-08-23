package com.ite.sws.domain.product.service;

import com.ite.sws.domain.product.vo.ProductVO;

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
 * </pre>
 */
public interface ProductService {
    ProductVO getProduct() throws Exception;
}
