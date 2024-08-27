package com.ite.sws.domain.product.controller;

import com.ite.sws.domain.product.dto.GetProductDetailRes;
import com.ite.sws.domain.product.service.ProductService;
import com.ite.sws.domain.product.vo.ProductVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 상품 컨트롤러
 * @author 정은지
 * @since 2024.08.23
 * @version 1.0
 *
 * <pre>
 * 수정일        	수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.23  	정은지        최초 생성
 * 2024.08.27  	구지웅        담당자 구지웅 기능 구현
 * </pre>
 */

@RequiredArgsConstructor
@RequestMapping(value = "/products")
@RestController
public class ProductController {

  private final ProductService productService;


  @GetMapping("/{productId}")
  public ResponseEntity<GetProductDetailRes> findProduct(@PathVariable Long productId) {
    return ResponseEntity.ok().body(productService.findProductDetail(productId));
  }

  @GetMapping
  public ResponseEntity<List<ProductVO>> findProductByName(@RequestParam String name) {
    return ResponseEntity.ok().body(productService.findProductsByProductName(name));
  }

  ///products?name=””
  ///products/{productId}/reviews?page=””&size=””


}
