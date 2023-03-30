package com.kh.product.web.form;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SaveForm {

  @NotBlank  //null과 빈문자열("")을 허용 안함, 문자열 타입에만 사용
  private String pname;
  @NotNull  //모든 타입에 대해 null허용 안함
  @Positive //양수
  private Long quantity;
  @NotNull
  @Positive //양수
  @Min(1000) //최소값
  private Long price;
}
