package com.ite.sws.domain.review.service;

import com.ite.sws.constant.UploadCommand;
import com.ite.sws.domain.review.mapper.ReviewBindingMapper;
import com.ite.sws.domain.review.mapper.ReviewMapper;
import com.ite.sws.domain.review.vo.ReviewVO;
import com.ite.sws.exception.CustomException;
import com.ite.sws.exception.ErrorCode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReviewPersistenceHelper extends AbstractPersistenceHelper<ReviewVO> {

    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewPersistenceHelper(MediaPersistLocationStrategy mediaPersistLocationStrategy,
        @Value("${cloud.aws.s3.review.bucket}") String reviewBucket, ReviewMapper reviewMapper) {
        super(mediaPersistLocationStrategy, reviewBucket);
        this.reviewMapper = reviewMapper;
    }

    @Override
    protected void processDomain(ReviewVO createRequest, List<String> fileIdentifiers,
        UploadCommand uploadCommand) {
        String shortFormIdentifier = fileIdentifiers.size() > 0 ? fileIdentifiers.get(0) : null;
        String thumbnailIdentifier = fileIdentifiers.size() > 1 ? fileIdentifiers.get(1) : null;

        ReviewVO reviewVO = ReviewBindingMapper.INSTANCE.combineIntoReviewVo(createRequest,
            thumbnailIdentifier,
            shortFormIdentifier);
        if (uploadCommand.compareTo(UploadCommand.CREATE) == 0) {
            reviewMapper.createReview(reviewVO);
        }
        else{
            throw new CustomException(ErrorCode.NO_COMMAND);
        }

    }

}
