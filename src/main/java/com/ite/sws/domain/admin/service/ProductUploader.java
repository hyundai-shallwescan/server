package com.ite.sws.domain.admin.service;

import com.ite.sws.domain.admin.dto.PostCreateProductReq;
import com.ite.sws.domain.admin.mapper.AdminBindingMapper;
import com.ite.sws.domain.admin.mapper.AdminMapper;
import com.ite.sws.domain.product.vo.ProductVO;
import com.ite.sws.domain.review.service.AbstractUploader;
import com.ite.sws.domain.review.service.S3Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProductUploader extends AbstractUploader<PostCreateProductReq> {

    private final AdminMapper adminMapper;

    @Autowired
    public ProductUploader(S3Service s3Service,
        @Value("${cloud.aws.s3.product.bucket}") String bucketName,
        AdminMapper adminMapper) {
        super(s3Service, bucketName);
        this.adminMapper = adminMapper;
    }

    @Override
    protected void processDomain(PostCreateProductReq createRequest, List<String> fileIdentifiers) {
        String thumbnailIdentifier = !fileIdentifiers.isEmpty() ? fileIdentifiers.get(0) : null;
        String descriptionIdentifier = fileIdentifiers.size() > 1 ? fileIdentifiers.get(1) : null;

        ProductVO product = AdminBindingMapper.INSTANCE.toProduct(createRequest,
            thumbnailIdentifier, descriptionIdentifier);
        adminMapper.insertProduct(product);
    }


}
