package com.jdum.commerce.sumysoul.web.rest;

import com.jdum.commerce.sumysoul.domain.Food;
import com.jdum.commerce.sumysoul.service.FoodService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/food")
@RequiredArgsConstructor
@Slf4j
public class FoodController {

  private final FoodService foodService;

  @GetMapping
  public Food get() {
    return foodService.get();
  }

  @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
  @PostMapping
  public Food add(@RequestBody Food food) {
    return foodService.add(food);
  }

  @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
  @PostMapping("/images")
  public Food uploadImages(@RequestParam("img") List<MultipartFile> files) {
    return foodService.uploadImages(files);
  }
}
