package com.ite.sws.domain.payment.mapper;

import com.ite.sws.domain.admin.dto.GetMemberPaymentHistoryRes;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {
  List<GetMemberPaymentHistoryRes> selectMemberPaymentHistory(Long memberId);
}
