package com.jdum.commerce.sumysoul.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdum.commerce.sumysoul.domain.Drinks;
import com.jdum.commerce.sumysoul.domain.Element;
import com.jdum.commerce.sumysoul.domain.LocalizedString;
import com.jdum.commerce.sumysoul.web.error.ApplicationException;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

  @Mock
  private AmazonS3 s3;
  @Mock
  private ObjectMapper mapper;

  @InjectMocks
  private ImageService imageService;

  private final String menuBucket = "test-bucket";
  private final String menuBucketPath = "images";

  @BeforeEach
  void setUp() {
    imageService = new ImageService(s3, mapper);
    ReflectionTestUtils.setField(imageService, "menuBucket", menuBucket);
    ReflectionTestUtils.setField(imageService, "menuBucketPath", menuBucketPath);
  }

  @Test
  void shouldUploadFileToS3() {
    var fileName = "menu.json";
    var content = new Drinks();
    var jsonContent = "{\"name\":\"Sample Element\"}";

    try {
      when(mapper.writeValueAsString(content)).thenReturn(jsonContent);
      when(s3.putObject(menuBucket, fileName, jsonContent)).thenReturn(new PutObjectResult());

      assertDoesNotThrow(() -> imageService.uploadFile(fileName, content));
      verify(s3).putObject(menuBucket, fileName, jsonContent);
    } catch (JsonProcessingException e) {
      fail("Json processing failed");
    }
  }

  @Test
  void shouldNotUploadFileToS3IfJsonIsIncorrect() {
    var fileName = "menu.json";
    var content = new Drinks();

    try {
      when(mapper.writeValueAsString(content)).thenThrow(new JsonProcessingException("Error") {
      });

      assertThrows(ApplicationException.class, () -> imageService.uploadFile(fileName, content));
    } catch (JsonProcessingException e) {
      fail("Exception mocking failed");
    }
  }

  @Test
  void shouldUpdateImageUrlIfImageIsNotUrl() {
    var content = new Drinks();
    content.setName(new LocalizedString("drinks", null));
    content.setImageUrl("imageName=image.png");

    List<Element> elements = List.of(content);
    try {
      when(s3.getUrl("%s/%s".formatted(menuBucket, menuBucketPath), "imageName=image.png")).thenReturn(
          URI.create("https://example.com/images/imageName=image.png").toURL());
      assertDoesNotThrow(() -> imageService.updateImages(elements));
      assertEquals("https://example.com/images/imageName=image.png", content.getImageUrl());
    } catch (Exception e) {
      fail("URL creation failed");
    }
  }

  @Test
  void shouldNotUpdateImageUrlIfImageIsUrl() {
    var content = new Drinks();
    content.setName(new LocalizedString("drinks", null));
    content.setImageUrl("https://example.com/imageName=image.png");

    List<Element> elements = List.of(content);

    try {
      assertDoesNotThrow(() -> imageService.updateImages(elements));
      verify(s3, never()).getUrl(anyString(), anyString());
    } catch (Exception e) {
      fail("URL creation failed");
    }
  }

  @Test
  void shouldNotUpdateImageUrlIfImageIsEmpty() {
    var content = new Drinks();
    content.setName(new LocalizedString("drinks", null));
    content.setImageUrl("");

    List<Element> elements = List.of(content);

    try {
      assertDoesNotThrow(() -> imageService.updateImages(elements));
      verify(s3, never()).getUrl(anyString(), anyString());
    } catch (Exception e) {
      fail("URL creation failed");
    }
  }
}
