package com.ite.sws.domain.review.mapper;

import com.ite.sws.domain.review.dto.GetReviewDetailRes;
import com.ite.sws.domain.review.vo.Review;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
/**
 * Mybatis Mapper interface
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
@Mapper
public interface ReviewMapper {

    void createReview(Review review);

    Optional<GetReviewDetailRes> getReviewDetail(Long reviewId);

    void deleteReview(Long reviewId);

}
