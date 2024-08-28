package com.ite.sws.domain.payment.mapper;

import com.ite.sws.domain.admin.dto.GetMemberPaymentHistoryRes;
import com.ite.sws.domain.admin.dto.GetSalesRes;
import com.ite.sws.domain.admin.dto.SalesCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
/**
 * Payment 관련 Mybatis Mapper
 * @since 2024.08.26
 * @author 구지웅
 * @version 1.0
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	구지웅      최초 생성 및 멤버 결제 내역 조회 기능 구현
 * </pre>
 *
 */
@Mapper
public interface PaymentMapper {

  List<GetMemberPaymentHistoryRes> selectMemberPaymentHistory(Long memberId);

  List<GetSalesRes> selectSalesByCriteria(SalesCriteria criteria);
}
