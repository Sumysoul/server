package com.jdum.commerce.sumysoul.service;

import com.jdum.commerce.sumysoul.domain.Drinks;
import com.jdum.commerce.sumysoul.domain.Element;
import com.jdum.commerce.sumysoul.repository.DrinksRepository;
import com.jdum.commerce.sumysoul.utils.MenuHelper;
import com.jdum.commerce.sumysoul.web.error.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class DrinksService {

  @Value("${application.s3.drinks-file}")
  private String menuFile;

  private final DrinksRepository repository;
  private final ImageService imageService;

  public Drinks get() {
    List<Drinks> drinks = repository.findAll();
    if (CollectionUtils.isEmpty(drinks)) {
      throw new NotFoundException("Drinks not found");
    }
    return drinks.getFirst();
  }

  public Drinks add(Drinks menu) {
    List<Element> elements = MenuHelper.flatten(menu);
    MenuHelper.updateSubIds(elements);
    imageService.updateImages(elements);
    imageService.uploadFile(menuFile, menu);
    return repository.save(menu);
  }
}
