package com.ite.sws.domain.admin.service;

import com.ite.sws.constant.S3Constant.Product;
import com.ite.sws.domain.admin.dto.PostCreateProductReq;
import com.ite.sws.domain.admin.mapper.AdminMapper;
import com.ite.sws.domain.product.mapper.ProductMapper;
import com.ite.sws.exception.CustomException;
import com.ite.sws.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Admin Service implementation
 *
 * @version 1.0
 * <pre>
 * 수정일 수정자 수정내용
 * =================== =========
 * 2024.08.26 구지웅 최초 생성
 * </pre>
 * @구지웅 구지웅
 * @since 2024.08.26
 **/


@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

  private final ProductUploader productUploader;
  private final AdminMapper adminMapper;
  private final ProductMapper productMapper;

  @Override
  public void addProduct(PostCreateProductReq postCreateProductReq, MultipartFile thumbnail,
      MultipartFile descriptionImage) {
    List<MultipartFile> list = new ArrayList<>();
    list.add(thumbnail);
    list.add(descriptionImage);
    productUploader.upload(postCreateProductReq, Product.PREFIX, list);
  }

  @Override
  public void deleteProduct(Long productId) {
    productMapper.selectProduct(productId).orElseThrow(() -> {
      throw new CustomException(ErrorCode.PRODUCT_IS_NOT_FOUND);
    });
    adminMapper.updateProductIsDeleted(productId);
  }
}
