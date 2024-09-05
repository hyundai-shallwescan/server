package com.ite.sws.domain.admin.service;


import com.ite.sws.domain.admin.dto.GetMemberPaymentHistoryRes;
import com.ite.sws.domain.admin.dto.GetPaymentHistoryRes;
import com.ite.sws.domain.admin.dto.GetSalesRes;
import com.ite.sws.domain.admin.dto.PatchProductReq;
import com.ite.sws.domain.admin.dto.PaymentHistoryCriteria;
import com.ite.sws.domain.admin.dto.PostCreateProductReq;
import com.ite.sws.domain.admin.dto.SalesCriteria;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * Admin 관련 서비스
 * @since 2024.08.26
 * @author 구지웅
 * @version 1.0
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	구지웅      최초 생성
 * 2024.08.27   구지웅      유저 결제 내역 조회 기능 구현, sales 조회 기능 구현
 * </pre>
 *
 */
public interface AdminService {

  void addProduct(PostCreateProductReq postCreateProductReq, List<MultipartFile> fileList);

  void deleteProduct(Long productId);

  void modifyProduct(Long productId, PatchProductReq patchProductReq, List<MultipartFile> fileList);

  List<GetMemberPaymentHistoryRes> findUserPaymentHistory(Long paymentId);

  List<GetSalesRes> findSalesByCriteria(SalesCriteria criteria);

  List<GetPaymentHistoryRes> findPaymentHistoryOnThatDay(PaymentHistoryCriteria paymentHistoryCriteria);

}
