package com.jdum.commerce.sumysoul.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
@Data
public class ErrorResponse implements Serializable {
  private String title;
  private int status;
  private Object details;
}
