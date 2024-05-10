package com.jdum.commerce.sumysoul.web.rest;

import com.jdum.commerce.sumysoul.domain.Drinks;
import com.jdum.commerce.sumysoul.service.DrinksService;
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
@RequestMapping("/api/v1/drinks")
@RequiredArgsConstructor
@Slf4j
public class DrinksController {

  private final DrinksService drinksService;

  @GetMapping
  public Drinks get() {
    return drinksService.get();
  }

  @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
  @PostMapping
  public Drinks add(@RequestBody Drinks drinks) {
    return drinksService.add(drinks);
  }

  @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
  @PostMapping("/images")
  public Drinks uploadImages(@RequestParam("img") List<MultipartFile> files) {
    return drinksService.uploadImages(files);
  }
}
