package com.ite.sws.domain.review.service;

import com.ite.sws.domain.review.constant.S3Constant.Review;
import com.ite.sws.domain.review.dto.PostCreateReviewReq;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
/**
 * Review관련 Service
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


@Service
public class ShallWeScanReviewService implements ReviewService {

    private final ReviewUploader reviewUploader;

    @Autowired
    public ShallWeScanReviewService(ReviewUploader reviewUploader) {
        this.reviewUploader = reviewUploader;
    }

    @Override
    public void createReview(PostCreateReviewReq createReviewReq, MultipartFile thumbnail,
                             MultipartFile shortForm) {
      List<MultipartFile> files = Arrays.asList(thumbnail, shortForm);

    String reviewBucketPrefix = Review.PREFIX;

    reviewUploader.upload(createReviewReq, reviewBucketPrefix, files);
    }
}