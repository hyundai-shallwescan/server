package com.ite.sws.domain.admin.mapper;

import com.ite.sws.domain.admin.dto.PostCreateProductReq;
import com.ite.sws.domain.product.vo.ProductVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminBindingMapper {

  AdminBindingMapper INSTANCE = Mappers.getMapper(AdminBindingMapper.class);

  @Mapping(target = "thumbnailImage", source = "thumbnailImage")
  @Mapping(target = "descriptionImage", source = "descriptionImage")
  @Mapping(target = "productId", ignore = true)
  ProductVO toProduct(PostCreateProductReq postCreateProductReq, String thumbnailImage,
      String descriptionImage);
}