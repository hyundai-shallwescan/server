package com.ite.sws.domain.review.controller;


import com.ite.sws.domain.review.dto.PostCreateReviewReq;
import com.ite.sws.domain.review.service.ReviewService;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @PostMapping(consumes = {"multipart/form-data"})
  public ResponseEntity<Void> createReview(
      @RequestPart MultipartFile shortForm,
      @RequestPart MultipartFile image,
      @RequestPart PostCreateReviewReq postCreateReviewReq)
  throws SQLException {
    reviewService.createReview(postCreateReviewReq, image,
        shortForm);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

}
