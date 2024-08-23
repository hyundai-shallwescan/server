package com.ite.sws.domain.product.controller;

import com.ite.sws.domain.product.service.ProductService;
import com.ite.sws.domain.product.vo.ProductVO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
 * </pre>
 */

@RestController
@RequestMapping(value="/product", produces= MediaType.APPLICATION_JSON_VALUE)
@Log4j
@AllArgsConstructor
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/test")
    public ProductVO getProduct() throws Exception {

        log.info("getProduct");
        ProductVO productVO = service.getProduct();
        return productVO;
    }
}
