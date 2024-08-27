package com.ite.sws.domain.review.mapper;


import com.ite.sws.domain.review.dto.PostCreateReviewReq;
import com.ite.sws.domain.review.vo.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapping을 할 때 Boilerplate 코드를 해결하기 위한 Mapstruct interface
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
public interface ReviewBindingMapper {

    ReviewBindingMapper INSTANCE = Mappers.getMapper(ReviewBindingMapper.class);


    @Mapping(target = "shortFormId", ignore = true)
    Review toEntity(PostCreateReviewReq postCreateReviewReq, String thumbnailImage,
        String shortFormUrl);


}
