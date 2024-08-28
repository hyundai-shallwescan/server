package com.ite.sws.domain.admin.controller;

import com.ite.sws.domain.admin.dto.GetMemberPaymentHistoryRes;
import com.ite.sws.domain.admin.dto.GetSalesRes;
import com.ite.sws.domain.admin.dto.PatchProductReq;
import com.ite.sws.domain.admin.dto.PostCreateProductReq;
import com.ite.sws.domain.admin.service.AdminService;
import com.ite.sws.domain.admin.dto.SalesCriteria;
import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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
 * 2024.08.28   구지웅      어드민 유저 sales 조회 기능 구현*
 * </pre>
 *
 */
@RequiredArgsConstructor
@RequestMapping("/admins")
@RestController
public class AdminController {

  private final AdminService adminService;

  @PutMapping(produces = {"multipart/form-data"}, value = "/products")
  public ResponseEntity<Void> addProduct(
      @RequestPart @Valid PostCreateProductReq postCreateReviewReq,
      @RequestPart MultipartFile thumbnail, @RequestPart MultipartFile descriptionImage) {
    List<MultipartFile> list = Arrays.asList(thumbnail, descriptionImage);
    adminService.addProduct(postCreateReviewReq, list);
    return ResponseEntity.status(201).build();
  }

  @DeleteMapping("/products/{productId}")
  public ResponseEntity<Void> deleteProduct(
      @PathVariable Long productId) {
    adminService.deleteProduct(productId);
    return ResponseEntity.status(200).build();
  }

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

  @GetMapping("/payments/members/{memberId}")
  public ResponseEntity<List<GetMemberPaymentHistoryRes>> findMemberPaymentHistory(
      @PathVariable Long memberId) {
    return ResponseEntity.ok().body(adminService.findUserPaymentHistory(memberId));
  }

  @GetMapping("/sales")
  public ResponseEntity<List<GetSalesRes>> findSaleByCriteria(
      @RequestParam(defaultValue = "2024") int year,
      @RequestParam(defaultValue = "08") int month,
      @RequestParam(defaultValue = "1") int day,
      @RequestParam(defaultValue = "M") char gender,
      @RequestParam(defaultValue = "30") int age) {

    SalesCriteria criteria = new SalesCriteria(year, month,day,gender,age);

    List<GetSalesRes> sales = adminService.findSalesByCriteria(criteria);

    return ResponseEntity.ok(sales);
  }

}
