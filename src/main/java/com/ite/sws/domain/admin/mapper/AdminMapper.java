package com.ite.sws.domain.admin.mapper;

import com.ite.sws.domain.product.vo.ProductVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
      void insertProduct(ProductVO productVO);
}
