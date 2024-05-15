package com.jdum.commerce.sumysoul.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jdum.commerce.sumysoul.domain.Food;
import com.jdum.commerce.sumysoul.repository.FoodRepository;
import com.jdum.commerce.sumysoul.web.error.NotFoundException;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class FoodServiceTest {

  public static final String MENU_FILE = "menuFile.json";
  @Mock
  private FoodRepository repository;
  @Mock
  private ImageService imageService;

  @InjectMocks
  private FoodService foodService;

  @BeforeEach
  void setUp() {
    foodService = new FoodService(repository, imageService);
    ReflectionTestUtils.setField(foodService, "menuFile", MENU_FILE);
  }

  @Test
  void shouldReturnMenu() {
    var expectedFood = new Food();
    when(repository.findAll()).thenReturn(Arrays.asList(expectedFood, new Food()));

    Food result = foodService.get();
    assertEquals(expectedFood, result);
  }

  @Test
  void shouldThrowNotFoundIfMenuNotExists() {
    when(repository.findAll()).thenReturn(Collections.emptyList());

    assertThrows(NotFoundException.class, () -> foodService.get());
  }

  @Test
  void shouldAddMenuAndUpdateImages() {
    var menu = new Food();

    doNothing().when(imageService).updateImages(anyList());
    doNothing().when(imageService).uploadFile(MENU_FILE, menu);
    when(repository.save(any())).thenReturn(menu);

    var result = foodService.add(menu);
    assertEquals(menu, result);
    verify(repository).save(any());
    verify(imageService).updateImages(anyList());
    verify(imageService).uploadFile(MENU_FILE, menu);
  }
}
