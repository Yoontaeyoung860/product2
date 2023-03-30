package com.kh.product.web;

import com.kh.product.dao.Product;
import com.kh.product.svc.ProductSVC;
import com.kh.product.web.form.DetailForm;
import com.kh.product.web.form.SaveForm;
import com.kh.product.web.form.UpdateForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j //로깅을 위한 어노테이션 (log.info() 등) 사용가능
@Controller //컨트롤러 어노테이션
@RequestMapping("/products") //상품관련 요청을 처리하는 컨트롤러
@RequiredArgsConstructor //final 필드를 가지고 생성자를 만들어줌
//생성자를 만들어주지 않으면 @Autowired를 사용해야 하지만, 생성자를 만들어주면 @Autowired를 사용하지 않아도 됨
public class ProductController {

  private final ProductSVC productSVC;

  //등록양식
  @GetMapping("/add")
  public String saveform(Model model){
    SaveForm saveForm = new SaveForm();
    model.addAttribute("saveForm",saveForm);
    return "product/saveForm";
  }

  //등록처리 (검증) - @Valid 어노테이션을 사용하여 검증 처리 가능 (검증에 실패하면 다시 등록양식으로 이동)
  @PostMapping("/add")
  public String save(
      @Valid @ModelAttribute SaveForm saveForm,
      BindingResult bindingResult,  //검증 결과를 담는 객체
      RedirectAttributes redirectAttributes
      ){
    log.info("saveForm={}",saveForm);

    //데이터 검증
    //검증에 실패하면 다시 등록양식으로 이동 검증에 실패하면 bindingResult에 에러정보가 담김 (에러코드, 에러메시지 등) - 에러코드는 messages.properties
    if(bindingResult.hasErrors()){
      log.info("bindingResult={}", bindingResult);
      return "product/saveForm";
    }



    // 글로벌오류
    // 총액(상품수량*단가) 1억원 초과금지 (글로벌오류) - 에러코드는 messages.properties (글로벌오류는 에러코드만 지정)
    if(saveForm.getQuantity() * saveForm.getPrice() > 10000000){
      bindingResult.reject("",null,"총액은 1억원을 초과할 수 없습니다.");
    }

    //검증에 실패하면 다시 등록양식으로 이동
    if(bindingResult.hasErrors()){
      log.info("bindingResult={}", bindingResult);
      return "product/saveForm";
    }

    //등록처리
    Product product = new Product();
    product.setPname(saveForm.getPname());
    product.setQuantity(saveForm.getQuantity());
    product.setPrice(saveForm.getPrice());
    Long savedpid = productSVC.save(product);
    //리다이렉트
    redirectAttributes.addAttribute("id",savedpid);
    return "redirect:/products/{id}/detail";
  }

  //조회
  @GetMapping("/{id}/detail")
  public String findById(
      @PathVariable("id") Long id,Model model)
  {
    Optional<Product> findedProduct = productSVC.findById(id);
    Product product = findedProduct.orElseThrow();

    DetailForm detailForm = new DetailForm();
    detailForm.setPid(product.getPid());
    detailForm.setPname(product.getPname());
    detailForm.setQuantity(product.getQuantity());
    detailForm.setPrice(product.getPrice());

    model.addAttribute("detailForm",detailForm);
    return "product/detailForm";
  }

  //수정화면 이동
  @GetMapping("/{id}/edit")
  public String updateForm(@PathVariable("id") Long id, Model model)
  {
    Optional<Product> findedProduct = productSVC.findById(id);
    //상품이 없으면 예외발생
    Product product = findedProduct.orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));
    //상품이 있으면 수정화면으로 이동
    //수정화면에서 상품정보를 수정할 수 있도록 수정화면에 상품정보를 전달
    UpdateForm updateForm = new UpdateForm();
    updateForm.setPid(product.getPid());
    updateForm.setPname(product.getPname());
    updateForm.setQuantity(product.getQuantity());
    updateForm.setPrice(product.getPrice());
    model.addAttribute("updateForm",updateForm);
    return "product/updateForm";
  }

  //수정 처리 - (수정화면에서 수정된 상품정보를 받아서 처리)
  @PostMapping("/{id}/edit")
  public String update(
      @PathVariable("id") Long pid,
      @Valid @ModelAttribute UpdateForm updateForm,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes
  ){
    //검증하기. 오류가 있으면 다시 수정화면으로 이동
    if(bindingResult.hasErrors()){
      log.info("bindingResult={}",bindingResult);
      return "product/updateForm";
    }

    //정상수정 처리
    Product product = new Product();
    product.setPid(pid);
    product.setPname(updateForm.getPname());
    product.setQuantity(updateForm.getQuantity());
    product.setPrice(updateForm.getPrice());
    productSVC.update(pid, product);
    redirectAttributes.addAttribute("id",pid);
    return "redirect:/products/{id}/detail";
  }

  //삭제 처리 - (상품번호를 받아서 삭제처리)
  @GetMapping("/{id}/del")
  public String deleteById(@PathVariable("id") Long pid){
    productSVC.delete(pid);

    return "redirect:/products";
  }

  //전체조회
  @GetMapping
  public String findAll(Model model){
    List<Product> products = productSVC.findAll();
    model.addAttribute("products",products);

    return "product/all";
  }

}
