package com.kh.product.web.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class DetailForm {
  private Long pid;
  @NotBlank
  private String pname;
  @NotNull
  @Range(min=1, max=1000)
  private Long quantity;
  @NotNull
  @Min(1000)
  private Long price;
}
