package com.ite.sws.domain.product.mapper;

import com.ite.sws.domain.product.vo.ProductVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {
    ProductVO selectProduct();
}
