package com.kh.product.web.api.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class EditReq {
  @NotBlank
  private String pname;
  @NotNull
  @Positive(message = "수량은 양수값")
  private Long quantity;
  @NotNull
  @Positive(message = "단가는 양수값")
  private Long price;
}