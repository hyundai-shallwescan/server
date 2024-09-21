package com.ite.sws.domain.review.service;

import com.ite.sws.constant.UploadCommand;
import com.ite.sws.domain.review.dto.GetReviewDetailRes;
import com.ite.sws.domain.review.dto.GetReviewRes;
import com.ite.sws.domain.review.dto.PostCreateReviewReq;
import com.ite.sws.domain.review.mapper.ReviewBindingMapper;
import com.ite.sws.domain.review.mapper.ReviewMapper;
import com.ite.sws.exception.CustomException;
import com.ite.sws.exception.ErrorCode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class ReviewServiceImpl implements ReviewService {

    private final ReviewPersistenceHelper reviewUploader;
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewServiceImpl(ReviewPersistenceHelper reviewUploader, ReviewMapper reviewMapper) {
        this.reviewUploader = reviewUploader;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public void createReview(PostCreateReviewReq createReviewReq, List<MultipartFile> fileList) {

        reviewUploader.upload(ReviewBindingMapper.INSTANCE.toReviewVo(createReviewReq),
            fileList,
            UploadCommand.CREATE);
    }

    @Override
    public List<GetReviewRes> getReviews(int size, int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("size", size);
        params.put("page", page);
        return reviewMapper.getReviews(params);
    }


    @Override
    public GetReviewDetailRes getReviewDetail(Long reviewId) {
        return reviewMapper.getReviewDetail(reviewId)
            .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_IS_NOT_FOUND));
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewMapper.getReviewDetail(reviewId)
            .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_IS_NOT_FOUND));
        reviewMapper.deleteReview(reviewId);
    }
}
