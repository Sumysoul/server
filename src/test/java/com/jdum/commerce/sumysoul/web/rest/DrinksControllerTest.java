package com.jdum.commerce.sumysoul.web.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdum.commerce.sumysoul.domain.Drinks;
import com.jdum.commerce.sumysoul.service.DrinksService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = {
    DrinksController.class,
    DrinksService.class,
    ObjectMapper.class
})
@AutoConfigureMockMvc(addFilters = false)
@EnableWebMvc
class DrinksControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private DrinksService drinksService;

  @Autowired
  private ObjectMapper mapper;

  @SneakyThrows
  @Test
  void shouldGetDrinks() {
    var menu = buildMenu();
    when(drinksService.get()).thenReturn(menu);
    mvc.perform(MockMvcRequestBuilders.get("/api/v1/drinks")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(mapper.writeValueAsString(menu)));
  }

  @SneakyThrows
  @Test
  void shouldAddDrinks() {
    var menu = buildMenu();
    when(drinksService.add(menu)).thenReturn(menu);
    mvc.perform(MockMvcRequestBuilders.post("/api/v1/drinks")
            .content(mapper.writeValueAsString(menu))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(mapper.writeValueAsString(menu)));
  }

  private Drinks buildMenu() {
    var menu = new Drinks();
    menu.setId("123");
    return menu;
  }
}
