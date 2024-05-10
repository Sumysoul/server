package com.jdum.commerce.sumysoul.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Item extends Element {
  private LocalizedString priceUnit;
  private LocalizedString volumeUnit;
  private List<PriceAndVolume> prices = new ArrayList<>();
  private List<Ingredient> ingredients = new ArrayList<>();
  private List<Ingredient> extra;
}
