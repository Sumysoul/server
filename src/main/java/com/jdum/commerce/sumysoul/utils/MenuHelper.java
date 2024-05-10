package com.jdum.commerce.sumysoul.utils;

import static java.util.Optional.ofNullable;

import com.jdum.commerce.sumysoul.domain.Element;
import com.jdum.commerce.sumysoul.domain.Ingredient;
import com.jdum.commerce.sumysoul.domain.Item;
import com.jdum.commerce.sumysoul.domain.Menu;
import com.jdum.commerce.sumysoul.domain.MenuGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

@UtilityClass
public class MenuHelper {

  public final String URL_PATTERN_REG_EXP = "^(http|https)://[^\\s/$.?#].[^\\s]*$";
  private final Pattern URL_MATCHER = Pattern.compile(URL_PATTERN_REG_EXP);

  public boolean isUrl(String src) {
    return URL_MATCHER.matcher(src).matches();
  }

  public void attachImgUrl(Menu menu, Map<String, String> map) {
    setImageUrl(menu, map);
    var groups = menu.getGroups();
    if (!CollectionUtils.isEmpty(groups)) {
      groups.forEach(i -> attachImgUrl(i, map));
    }
  }

  private void attachImgUrl(MenuGroup group, Map<String, String> map) {
    setImageUrl(group, map);
    var innerGroups = group.getGroups();
    if (!CollectionUtils.isEmpty(innerGroups)) {
      innerGroups.forEach(i -> attachImgUrl(i, map));
    }
    var items = group.getItems();
    if (!CollectionUtils.isEmpty(items)) {
      items.forEach(i -> attachImgUrl(i, map));
    }
  }

  private void attachImgUrl(Item item, Map<String, String> map) {
    setImageUrl(item, map);
    var ingredients = item.getIngredients();
    if (!CollectionUtils.isEmpty(ingredients)) {
      ingredients.forEach(i -> setImageUrl(i, map));
    }
    var extra = item.getExtra();
    if (!CollectionUtils.isEmpty(extra)) {
      extra.forEach(i -> setImageUrl(i, map));
    }
  }

  public void updateSubIds(List<Element> elements) {
    elements.forEach(element -> element.setSubId(UUID.randomUUID().toString()));
  }

  private void setImageUrl(Element element, Map<String, String> map) {
    ofNullable(map.get(element.getImageUrl()))
        .ifPresent(element::setImageUrl);
  }

  public List<Element> flatten(Menu menu) {
    List<Element> acc = new ArrayList<>();
    acc.add(menu);
    var groups = menu.getGroups();
    if (!CollectionUtils.isEmpty(groups)) {
      groups.forEach(group -> flatten(group, acc));
    }
    return acc;
  }

  public void flatten(MenuGroup menuGroup, List<Element> acc) {
    acc.add(menuGroup);
    var groups = menuGroup.getGroups();
    if (!CollectionUtils.isEmpty(groups)) {
      groups.forEach(group -> flatten(group, acc));
    }
    var items = menuGroup.getItems();
    if (!CollectionUtils.isEmpty(items)) {
      items.forEach(item -> flatten(item, acc));
    }
  }

  public void flatten(Item item, List<Element> acc) {
    acc.add(item);
    var ingredients = item.getIngredients();
    if (!CollectionUtils.isEmpty(ingredients)) {
      acc.addAll(ingredients);
    }
    var extra = item.getExtra();
    if (!CollectionUtils.isEmpty(extra)) {
      acc.addAll(extra);
    }
  }
}
