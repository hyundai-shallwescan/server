package com.ite.sws.domain.admin.mapper;

import com.ite.sws.domain.admin.dto.PatchProductReq;
import com.ite.sws.domain.admin.dto.PostCreateProductReq;
import com.ite.sws.domain.product.vo.ProductVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Boiler plate코드를 해결하기위한 Mapstruct Interface
 * @since 2024.08.26
 * @author 구지웅
 * @version 1.0
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	구지웅      최초 생성
 * </pre>
 *
 */

@Mapper
public interface AdminBindingMapper {

  AdminBindingMapper INSTANCE = Mappers.getMapper(AdminBindingMapper.class);

  @Mapping(target = "thumbnailImage", source = "thumbnailImage")
  @Mapping(target = "descriptionImage", source = "descriptionImage")
  ProductVO combineIntoVo(ProductVO productVO, String thumbnailImage,
      String descriptionImage);

  ProductVO toWithoutThumbnailAndDescriptionImage(PostCreateProductReq postCreateProductReq);

  ProductVO toWithoutThumbnailAndDescriptionImage(PatchProductReq patchProductReq, Long productId);
}