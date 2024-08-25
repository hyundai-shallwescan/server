package com.ite.sws.domain.review.controller;


import com.ite.sws.domain.review.dto.GetReviewDetailRes;
import com.ite.sws.domain.review.dto.PostCreateReviewReq;
import com.ite.sws.domain.review.service.ReviewService;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
/**
 * Review관련 Controller
 *
 * @author 구지웅
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.24  	구지웅      최초 생성
 * </pre>
 * @since 2024.08.24
 */
@RequiredArgsConstructor
@RestController
public class ReviewController {

  private final ReviewService reviewService;

  @PostMapping(consumes = {"multipart/form-data"},value = "/reviews")
  public ResponseEntity<Void> createReview(
      @RequestPart MultipartFile shortForm,
      @RequestPart MultipartFile image,
      @RequestPart PostCreateReviewReq postCreateReviewReq) {
    reviewService.createReview(postCreateReviewReq, image,
        shortForm);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/reviews/{reviewId}")
  public ResponseEntity<GetReviewDetailRes> getReviewDetail(@PathVariable Long reviewId) {
    return ResponseEntity.ok(reviewService.getReviewDetail(reviewId));
  }


}
