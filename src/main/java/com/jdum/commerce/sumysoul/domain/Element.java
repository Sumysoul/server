package com.jdum.commerce.sumysoul.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Element {
  private String subId;
  private LocalizedString name;
  private LocalizedString description;
  private String imageUrl;
  private boolean available = true;
  private Set<String> tags;
}