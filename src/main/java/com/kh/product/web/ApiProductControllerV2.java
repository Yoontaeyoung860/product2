package com.kh.product.web;

import com.kh.product.dao.Product;
import com.kh.product.svc.ProductSVC;
import com.kh.product.web.api.ApiResponse;
import com.kh.product.web.api.product.AddReq;
import com.kh.product.web.api.product.EditReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
//@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiProductControllerV2 {
  private final ProductSVC productSVC;
  //  등록	POST	/api/products
  @ResponseBody
  //client 요청과는 상관 없이 sercer에서 json으로 고정
  @PostMapping(value = "/products", produces = "application/json")
  public ApiResponse<Long> add(@RequestBody AddReq addReq) {
    log.info("reqMsg={}", addReq);
    //검증

    //AddReq를 product로 변환
    Product product = new Product();
    BeanUtils.copyProperties(addReq, product);

    //상품등록
    Long id = productSVC.save(product);
    //응답메시지
    return ApiResponse.createApiREsMsg("00", "성공", id);
  }

  //  조회	GET	/api/products/{id}
  @ResponseBody
  @GetMapping("/products/{id}")
  public ApiResponse<Product> findById(@PathVariable("id") Long id) {
    //상품 조회
    Optional<Product> findedProduct = productSVC.findById(id);
    //응답 메시지
    ApiResponse<Product> response = null;
    if (findedProduct.isPresent()) {
      response = ApiResponse.createApiREsMsg("00", "성공", findedProduct.get());
    } else {
      response = ApiResponse.createApiREsMsg("99", "찾는 상품이 없음", null);
    }
    return response;
  }
  //  수정	PATCH	/api/products/{id}
  @ResponseBody
  @PatchMapping("/products/{id}")
  public ApiResponse<Product> edit(@PathVariable("id") Long id, @RequestBody EditReq editReq) {

    //검증
    Optional<Product> findedProduct = productSVC.findById(id);
    if (findedProduct.isEmpty()) {
      return ApiResponse.createApiREsMsg("99", "찾는 정보가 없음", null);
    }

    //EditReq = > Product 변환
    Product product = new Product();
    BeanUtils.copyProperties(editReq,product);
    //수정
    productSVC.update(id, product);
    //응답메시지
    return ApiResponse.createApiREsMsg("00", "성공", productSVC.findById(id).get());
  }

  //  삭제	DELETE	/api/products/{id}
  @ResponseBody
  @DeleteMapping("/products/{id}")
  public ApiResponse<Product> del(@PathVariable("id") Long id) {
    //검증
    Optional<Product> findedProduct = productSVC.findById(id);
    if (findedProduct.isEmpty()) {
      return ApiResponse.createApiREsMsg("99", "삭제하고자 하는 상품이 없음", null);
    }
    //삭제
    productSVC.delete(id);

    //응답 메시지
    return ApiResponse.createApiREsMsg("00", "성공", productSVC.findById(id).get());
  }

  //  목록	GET	/api/products
  @ResponseBody
  @GetMapping("/products")
  public ApiResponse<List<Product>> findAll() {
    List<Product> list = productSVC.findAll();

    //api응답 메시지
    return ApiResponse.createApiREsMsg("00", "성공", list);
  }
}