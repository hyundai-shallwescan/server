package com.ite.sws.domain.product.mapper;

import com.ite.sws.domain.product.dto.GetProductDetailRes;
import com.ite.sws.domain.product.vo.ProductVO;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 상품 컨트롤러
 * @author 구지웅
 * @since 2024.08.27
 * @version 1.0
 *
 * <pre>
 * 수정일        	수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.27  	구지웅        담당자 구지웅 기능 구현
 * 2024.08.26  	김민정        상품이 존재하는지 확인
 * </pre>
 */
@Mapper
public interface ProductMapper {

    Optional<ProductVO> selectProduct(Long productId);

    Optional<GetProductDetailRes> selectProductDetail(Long productId);

    List<ProductVO> selectProductsByProductName(String productName);

    /**
     * 상품이 존재하는지 확인
     * @param productId 상품 ID
     * @return
     */
    int selectCountByProductId(@Param("productId") Long productId);
}
