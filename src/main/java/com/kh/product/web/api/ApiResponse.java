package com.kh.product.web.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
  private Header header;
  private T data;
  @Data
  @AllArgsConstructor
  public static class Header {
    private String rtcd;    //응답 코드 "00", "99"
    private String rtmsg;   //응답 메시지 "성공, "실패"
  }

  /**
   * api 응답 메시지 생성
   * @param rtcd 응답코드
   * @param rtmsg 응답메시지
   * @param data 데이터
   * @return
   * @param <T> 데이터
   */
  public static <T> ApiResponse<T> createApiREsMsg(String rtcd, String rtmsg, T data) {
//      ApiResponse<T> response = null;
//      Header header = new Header(rtcd, rtmsg);
//      response = new ApiResponse<>(header, data);
//      return response;
    return new ApiResponse<>(new Header(rtcd, rtmsg), data);

  }
}