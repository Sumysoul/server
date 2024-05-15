package com.jdum.commerce.sumysoul.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jdum.commerce.sumysoul.domain.Drinks;
import com.jdum.commerce.sumysoul.repository.DrinksRepository;
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
class DrinksServiceTest {

  public static final String MENU_FILE = "menuFile.json";
  @Mock
  private DrinksRepository repository;
  @Mock
  private ImageService imageService;

  @InjectMocks
  private DrinksService drinksService;

  @BeforeEach
  void setUp() {
    drinksService = new DrinksService(repository, imageService);
    ReflectionTestUtils.setField(drinksService, "menuFile", MENU_FILE);
  }

  @Test
  void shouldReturnMenu() {
    var expectedDrinks = new Drinks();
    when(repository.findAll()).thenReturn(Arrays.asList(expectedDrinks, new Drinks()));

    var result = drinksService.get();
    assertEquals(expectedDrinks, result);
  }

  @Test
  void shouldThrowNotFoundIfMenuNotExists() {
    when(repository.findAll()).thenReturn(Collections.emptyList());

    assertThrows(NotFoundException.class, () -> drinksService.get());
  }

  @Test
  void shouldAddMenuAndUpdateImages() {
    var menu = new Drinks();

    doNothing().when(imageService).updateImages(anyList());
    doNothing().when(imageService).uploadFile(MENU_FILE, menu);
    when(repository.save(any())).thenReturn(menu);

    var result = drinksService.add(menu);
    assertEquals(menu, result);
    verify(repository).save(any());
    verify(imageService).updateImages(anyList());
    verify(imageService).uploadFile(MENU_FILE, menu);
  }
}
