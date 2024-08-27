package com.ite.sws.domain.product.mapper;

import com.ite.sws.domain.product.dto.GetProductDetailRes;
import com.ite.sws.domain.product.vo.ProductVO;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {
    Optional<ProductVO> selectProduct(Long productId);
    Optional<GetProductDetailRes> selectProductDetail(Long productId);
}
