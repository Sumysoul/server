package com.jdum.commerce.sumysoul.service;

import com.jdum.commerce.sumysoul.domain.Element;
import com.jdum.commerce.sumysoul.domain.Food;
import com.jdum.commerce.sumysoul.repository.FoodRepository;
import com.jdum.commerce.sumysoul.utils.MenuHelper;
import com.jdum.commerce.sumysoul.web.error.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FoodService {

  @Value("${application.s3.food-file}")
  private String menuFile;

  private final FoodRepository repository;
  private final ImageService imageService;

  public Food get() {
    List<Food> food = repository.findAll();
    if (CollectionUtils.isEmpty(food)) {
      throw new NotFoundException("Food not found");
    }
    return food.getFirst();
  }

  public Food add(Food menu) {
    List<Element> elements = MenuHelper.flatten(menu);
    MenuHelper.updateSubIds(elements);
    imageService.updateImages(elements);
    imageService.uploadFile(menuFile, menu);
    return repository.save(menu);
  }

  public Food uploadImages(List<MultipartFile> files) {
    imageService.uploadFiles(files);
    var menu = get();
    return add(menu);
  }
}
