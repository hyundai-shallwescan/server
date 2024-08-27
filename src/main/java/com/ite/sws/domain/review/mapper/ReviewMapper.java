package com.ite.sws.domain.review.mapper;

import com.ite.sws.domain.product.dto.GetProductReviewRes;
import com.ite.sws.domain.review.dto.GetReviewDetailRes;
import com.ite.sws.domain.review.dto.GetReviewRes;
import com.ite.sws.domain.review.vo.ReviewVO;
import java.util.List;
import java.util.Map;
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
 * 2024.08.24  	구지웅      최초 생성 및 구현
 * </pre>
 * @since 2024.08.24
 */
@Mapper
public interface ReviewMapper {

    void createReview(ReviewVO reviewVO);

    List<GetReviewRes> getReviews(Map<String, Object> pagination);

    Optional<GetReviewDetailRes> getReviewDetail(Long reviewId);

    void deleteReview(Long reviewId);

    List<GetProductReviewRes> findProductReviews(Map<String,Object> pagination);
}
