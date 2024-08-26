package com.ite.sws.domain.admin.service;


import com.ite.sws.domain.admin.dto.PostCreateProductReq;
import org.springframework.web.multipart.MultipartFile;


public interface AdminService {
   void addProduct(PostCreateProductReq postCreateProductReq, MultipartFile thumbnail,
      MultipartFile descriptionImage);
   void deleteProduct(Long productId);
}
