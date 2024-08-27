package com.ite.sws.domain.admin.mapper;

import com.ite.sws.domain.product.vo.ProductVO;
import org.apache.ibatis.annotations.Mapper;


/**
 * Mybatis Admin Mapper
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
public interface AdminMapper {
      void insertProduct(ProductVO productVO);
      void updateProductIsDeleted(Long productId);
      void updateProduct(ProductVO productVO);
}
