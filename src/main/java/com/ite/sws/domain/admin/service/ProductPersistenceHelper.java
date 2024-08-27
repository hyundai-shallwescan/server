package com.ite.sws.domain.admin.service;

import com.ite.sws.constant.UploadCommand;
import com.ite.sws.domain.admin.mapper.AdminBindingMapper;
import com.ite.sws.domain.admin.mapper.AdminMapper;
import com.ite.sws.domain.product.vo.ProductVO;
import com.ite.sws.domain.review.service.AbstractPersistenceHelper;
import com.ite.sws.domain.review.service.MediaPersistLocationStrategy;
import com.ite.sws.exception.CustomException;
import com.ite.sws.exception.ErrorCode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Product 관련 사진 정보를 업데이트 하기 위한 헬퍼 클래스
 * @since 2024.08.26
 * @author 구지웅
 * @version 1.0
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	구지웅      최초 생성
 * </pre>
 *
 */

@Component
public class ProductPersistenceHelper extends AbstractPersistenceHelper<ProductVO> {

    private final AdminMapper adminMapper;

    @Autowired
    public ProductPersistenceHelper(MediaPersistLocationStrategy mediaPersistLocationStrategy,
        @Value("${cloud.aws.s3.product.bucket}") String bucketName,
        AdminMapper adminMapper) {
        super(mediaPersistLocationStrategy, bucketName);
        this.adminMapper = adminMapper;
    }

    @Override
    protected void processDomain(ProductVO productVO, List<String> fileIdentifiers,
        UploadCommand uploadCommand) {
        String thumbnailIdentifier = !fileIdentifiers.isEmpty() ? fileIdentifiers.get(0) : null;
        String descriptionIdentifier = fileIdentifiers.size() > 1 ? fileIdentifiers.get(1) : null;

        ProductVO updatedProduct = AdminBindingMapper.INSTANCE.combineIntoVo(productVO,
            thumbnailIdentifier, descriptionIdentifier);
        if (uploadCommand.compareTo(UploadCommand.CREATE) == 0) {
            adminMapper.insertProduct(updatedProduct);
        } else if (uploadCommand.compareTo(UploadCommand.UPDATE) == 0) {
            adminMapper.updateProduct(updatedProduct);
        } else {
            throw new CustomException(ErrorCode.NO_COMMAND);
        }

    }


}
