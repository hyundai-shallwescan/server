package com.ite.sws.domain.admin.controller;

import com.ite.sws.domain.admin.dto.GetMemberPaymentHistoryRes;
import com.ite.sws.domain.admin.dto.GetPaymentHistoryRes;
import com.ite.sws.domain.admin.dto.GetSalesRes;
import com.ite.sws.domain.admin.dto.PatchProductReq;
import com.ite.sws.domain.admin.dto.PaymentEvent;
import com.ite.sws.domain.admin.dto.PaymentHistoryCriteria;
import com.ite.sws.domain.admin.dto.PostCreateProductReq;
import com.ite.sws.domain.admin.dto.SalesCriteria;
import com.ite.sws.domain.admin.service.AdminService;
import com.ite.sws.domain.admin.service.WebFluxAsyncPaymentInfoEventPublisher;
import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

/**
 * Review관련 Controller
 * @since 2024.08.26
 * @author 구지웅
 * @version 1.0
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	구지웅      최초 생성 및 상품 관련 기능 구현
 * 2024.08.27   구지웅      유저 결제 내역 조회 기능 구현
 * 2024.08.28   구지웅      어드민 유저 sales 조회 기능 구현
 * 2024.09.05   구지웅      DeleteMapping method naming 수정
 * </pre>
 *
 */
@RequiredArgsConstructor
@RequestMapping("/admins")
@RestController
public class AdminController {

  private final WebFluxAsyncPaymentInfoEventPublisher retrievePaymentEvents;
  private final AdminService adminService;

  /**
   * *
   * @param postCreateReviewReq text 형식의 requestBody
   * @param thumbnail image 타입의 thumbnail
   * @param descriptionImage image 타입의 descriptionImage
   * @return Void
   */
  @PutMapping(produces = {"multipart/form-data"}, value = "/products")
  public ResponseEntity<Void> addProduct(
      @RequestPart @Valid PostCreateProductReq postCreateReviewReq,
      @RequestPart MultipartFile thumbnail, @RequestPart MultipartFile descriptionImage) {
    List<MultipartFile> list = Arrays.asList(thumbnail, descriptionImage);
    adminService.addProduct(postCreateReviewReq, list);
    return ResponseEntity.status(201).build();
  }

  /**
   * *
   * @param productId 삭제할 상품 아이디
   * @return Void
   */
  @DeleteMapping("/products/{productId}")
  public ResponseEntity<Void> updateProductDeleteStatus(
      @PathVariable Long productId) {
    adminService.modifyProductIsDeletedToggle(productId);
    return ResponseEntity.status(200).build();
  }

  /**
   * *
   * @param productId 식별아이디, productId
   * @param patchProductReq 변경할 항목의 text based requestBody
   * @param thumbnail image 타입의 thumbnail
   * @param descriptionImage image 타입의 descriptionImage
   * @return Void
   */
  @PatchMapping("/products/{productId}")
  public ResponseEntity<Void> modifyProduct(
      @PathVariable Long productId,
      @RequestPart @Valid PatchProductReq patchProductReq,
      @RequestPart MultipartFile thumbnail,
      @RequestPart MultipartFile descriptionImage) {

    List<MultipartFile> fileList = Arrays.asList(thumbnail, descriptionImage);
    adminService.modifyProduct(productId, patchProductReq, fileList);
    return ResponseEntity.status(200).build();
  }

  /**
   * *
   * @param paymentId 식별할 paymentId
   * @return GetMemberPaymentHistoryRes.java
   */
  @GetMapping("/payments/{paymentId}")
  public ResponseEntity<GetMemberPaymentHistoryRes> findMemberPaymentHistory(
      @PathVariable Long paymentId) {
    return ResponseEntity.ok().body(adminService.findUserPaymentHistory(paymentId));
  }

  /**
   * *
   * @param year 입력 년도
   * @param month 입력 달
   * @return List<GetSalesRes>
   */
  @GetMapping("/sales")
  public ResponseEntity<List<GetSalesRes>> findSaleByCriteria(
      @RequestParam(defaultValue = "2024") int year,
      @RequestParam(defaultValue = "08") int month
  ) {
    SalesCriteria criteria = SalesCriteria.of(year, month);
    List<GetSalesRes> sales = adminService.findSalesByCriteria(criteria);
    return ResponseEntity.ok(sales);
  }

  /**
   * *
   * @param page 페이지 입력
   * @param size 사이즈 입력
   * @param year 입력 년도
   * @param month 입력 월
   * @param day 입력 날짜
   * @return Flux<PaymentEvent>
   */
  @GetMapping(value = "/payments/members", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<PaymentEvent> streamUserPayments(
      @RequestParam(value = "page",defaultValue = "0") int page,
      @RequestParam(value = "size",defaultValue = "10") int size,
      @RequestParam("year") int year,
      @RequestParam(value = "month") int month,
      @RequestParam("day") int day) {
    List<GetPaymentHistoryRes> initData = adminService.findPaymentHistoryOnThatDay(
        PaymentHistoryCriteria.of(page, size, year, month, day));
    Flux<PaymentEvent> initialPayments = Flux.fromIterable(initData)
        .map(this::convertToPaymentEvent);
    Flux<PaymentEvent> realTimePayments = retrievePaymentEvents.retrievePaymentEvents();
    return initialPayments.concatWith(realTimePayments);
  }

  /**
   * helper method
   * @param history
   * @return PaymentInfo
   */
  private PaymentEvent convertToPaymentEvent(GetPaymentHistoryRes history) {
    return PaymentEvent.of(history.getPaymentId(), history.getUserId(), history.getUserName(),
        history.getCreatedAt());
  }

}
