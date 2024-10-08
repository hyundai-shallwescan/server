package com.ite.sws.domain.review.service;

import com.ite.sws.domain.review.dto.GetReviewDetailRes;
import com.ite.sws.domain.review.dto.GetReviewRes;
import com.ite.sws.domain.review.dto.PostCreateReviewReq;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
/**
 * Review관련 Service interface
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
public interface ReviewService {

  void createReview(PostCreateReviewReq createReviewReq, List<MultipartFile> fileList);

  List<GetReviewRes> getReviews(int page, int size);

  GetReviewDetailRes getReviewDetail(Long reviewId);

  void deleteReview(Long reviewId);

}
