package com.ite.sws.domain.review.service;

import com.ite.sws.domain.review.dto.PostCreateReviewReq;
import com.ite.sws.domain.review.exception.ReviewErrorCode;
import com.ite.sws.domain.review.exception.ReviewException;
import com.ite.sws.domain.review.mapper.ReviewBindingMapper;
import com.ite.sws.domain.review.mapper.ReviewMapper;
import java.util.List;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ReviewUploader extends AbstractUploader<PostCreateReviewReq> {

    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewUploader(S3Service s3Service,@Value("${cloud.aws.s3.review.bucket}") String reviewBucket, ReviewMapper reviewMapper) {
        super(s3Service, reviewBucket);
        this.reviewMapper = reviewMapper;
    }

    @Override
    protected void processDomain(PostCreateReviewReq createRequest, List<String> fileIdentifiers) {
        String thumbnailIdentifier = fileIdentifiers.size() > 0 ? fileIdentifiers.get(0) : null;
        String shortFormIdentifier = fileIdentifiers.size() > 1 ? fileIdentifiers.get(1) : null;

        reviewMapper.createReview(
            ReviewBindingMapper.INSTANCE.toEntity(createRequest, thumbnailIdentifier, shortFormIdentifier)
        );
    }

    @Override
    protected void handlePersistenceException(PersistenceException e) {
        throw new ReviewException(ReviewErrorCode.REVIEW_CANT_BE_PERSIST);
    }

    @Override
    protected void handleGeneralException(Exception e) {
        throw new ReviewException(ReviewErrorCode.SYSTEM_ERROR);
    }
}
