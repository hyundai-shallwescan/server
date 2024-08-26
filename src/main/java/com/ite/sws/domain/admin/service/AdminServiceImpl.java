package com.ite.sws.domain.admin.service;

import com.ite.sws.constant.UploadCommand;
import com.ite.sws.domain.admin.dto.PatchProductReq;
import com.ite.sws.domain.admin.dto.PostCreateProductReq;
import com.ite.sws.domain.admin.mapper.AdminBindingMapper;
import com.ite.sws.domain.admin.mapper.AdminMapper;
import com.ite.sws.domain.product.mapper.ProductMapper;
import com.ite.sws.domain.product.vo.ProductVO;
import com.ite.sws.exception.CustomException;
import com.ite.sws.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Admin 관련 서비스 Impl
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

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

  private final ProductPersistenceHelper productUploader;
  private final AdminMapper adminMapper;
  private final ProductMapper productMapper;

  @Override
  public void addProduct(PostCreateProductReq postCreateProductReq, List<MultipartFile> fileList) {
    productUploader.upload(
        AdminBindingMapper.INSTANCE.toWithoutThumbnailAndDescriptionImage(postCreateProductReq),
        fileList, UploadCommand.CREATE);
  }

  @Override
  public void deleteProduct(Long productId) {
    findProductHelper(productId);
    adminMapper.updateProductIsDeleted(productId);
  }

  @Override
  public void modifyProduct(Long productId, PatchProductReq patchProductReq,
      List<MultipartFile> fileList) {
    findProductHelper(productId);
    productUploader.upload(
        AdminBindingMapper.INSTANCE.toWithoutThumbnailAndDescriptionImage(patchProductReq,productId),
        fileList, UploadCommand.UPDATE);

  }


  private ProductVO findProductHelper(Long productId) {
    return productMapper.selectProduct(productId).orElseThrow(() -> {
      throw new CustomException(ErrorCode.PRODUCT_IS_NOT_FOUND);
    });

  }


}
