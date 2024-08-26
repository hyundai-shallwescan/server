package com.ite.sws.domain.admin.controller;

import com.ite.sws.domain.admin.dto.PostCreateProductReq;
import com.ite.sws.domain.admin.service.AdminService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Admin Contoller
 *
 * @version 1.0
 *
 * <pre>
 * 수정일 수정자 수정내용
 * =================== =========
 * 2024.08.26 구지웅 최초 생성
 * </pre>
 * @구지웅 구지웅
 * @since 2024.08.26
 **/
@RequiredArgsConstructor
@RestController
public class AdminController {

  private final AdminService adminService;

  @PutMapping(produces = {"multipart/form-data"},value = "/admins/products")
  public ResponseEntity<Void> addProduct(
      @RequestPart @Valid PostCreateProductReq postCreateReviewReq,
      @RequestPart MultipartFile thumbnail, @RequestPart MultipartFile descriptionImage) {
    adminService.addProduct(postCreateReviewReq,thumbnail,descriptionImage);
    return ResponseEntity.status(201).build();
  }


}
