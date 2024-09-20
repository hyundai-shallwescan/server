package com.ite.sws.domain.admin.mapper;

import com.ite.sws.domain.admin.dto.GetMemberPaymentHistoryRes;
import com.ite.sws.domain.admin.dto.GetPaymentHistoryRes;
import com.ite.sws.domain.admin.dto.GetSalesRes;
import com.ite.sws.domain.admin.dto.PaymentHistoryCriteria;
import com.ite.sws.domain.admin.dto.SalesCriteria;
import com.ite.sws.domain.product.vo.ProductVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


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

      GetMemberPaymentHistoryRes selectMemberPaymentHistory(Long paymentId);

      List<GetSalesRes> selectSalesByCriteria(SalesCriteria criteria);

      List<GetPaymentHistoryRes> selectPaymentHistory(PaymentHistoryCriteria criteria);
}
