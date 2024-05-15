package com.jdum.commerce.sumysoul.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jdum.commerce.sumysoul.domain.Element;
import com.jdum.commerce.sumysoul.domain.Food;
import com.jdum.commerce.sumysoul.domain.Item;
import com.jdum.commerce.sumysoul.domain.MenuGroup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class MenuHelperTest {

  @Test
  void shouldReturnTrueIfUrlIsValid() {
    assertTrue(MenuHelper.isUrl("http://example.com"));
    assertTrue(MenuHelper.isUrl("https://example.com/resource"));
  }

  @Test
  void shouldReturnFalseIfUrlIsValid() {
    assertFalse(MenuHelper.isUrl("example"));
    assertFalse(MenuHelper.isUrl("http://"));
    assertFalse(MenuHelper.isUrl("https://example.com with space"));
  }

  @Test
  void shouldAttachNewUrl() {
    var menu = new Food();
    menu.setImageUrl("oldUrl");
    Map<String, String> urlMap = new HashMap<>();
    urlMap.put("oldUrl", "newUrl");

    MenuHelper.attachImgUrl(menu, urlMap);
    assertEquals("newUrl", menu.getImageUrl());
  }

  @Test
  void shouldAttachAllUrls() {
    var menu = new Food();
    menu.setImageUrl("oldMenuUrl");
    var group = new MenuGroup();
    group.setImageUrl("oldGroupUrl");
    var item = new Item();
    item.setImageUrl("oldItemUrl");
    menu.setGroups(Collections.singletonList(group));
    group.setItems(Collections.singletonList(item));

    Map<String, String> urlMap = new HashMap<>();
    urlMap.put("oldMenuUrl", "newMenuUrl");
    urlMap.put("oldGroupUrl", "newGroupUrl");
    urlMap.put("oldItemUrl", "newItemUrl");

    MenuHelper.attachImgUrl(menu, urlMap);

    assertEquals("newMenuUrl", menu.getImageUrl());
    assertEquals("newGroupUrl", group.getImageUrl());
    assertEquals("newItemUrl", item.getImageUrl());
  }

  @Test
  void shouldUpdateSubId() {
    List<Element> elements = new ArrayList<>();
    elements.add(new Food());
    elements.add(new MenuGroup());

    MenuHelper.updateSubIds(elements);

    for (Element element : elements) {
      assertNotNull(element.getSubId());
    }
  }

  @Test
  void shouldFlattenMenu() {
    var menu = new Food();
    var group = new MenuGroup();
    var item = new Item();
    menu.setGroups(Collections.singletonList(group));
    group.setItems(Collections.singletonList(item));

    List<Element> flattened = MenuHelper.flatten(menu);

    assertTrue(flattened.contains(menu));
    assertTrue(flattened.contains(group));
    assertTrue(flattened.contains(item));
  }

}
