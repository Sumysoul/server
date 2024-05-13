package com.jdum.commerce.sumysoul.service;

import static java.util.Optional.ofNullable;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdum.commerce.sumysoul.domain.Element;
import com.jdum.commerce.sumysoul.utils.MenuHelper;
import com.jdum.commerce.sumysoul.web.error.ApplicationException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

  private final AmazonS3 s3;
  private final ObjectMapper mapper;

  @Value("${application.s3.menu-bucket}")
  private String menuBucket;

  public void uploadFile(String fileName, Object content) {
    log.info("Update content for file: {} started", fileName);
    String json = convert(content);
    s3.putObject(menuBucket, fileName, json);
    log.info("Update content for file: {} completed", fileName);
  }

  private String convert(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      var message = "Unable to convert content to string";
      log.error(message);
      throw new ApplicationException(message, e);
    }
  }

  public void updateImages(List<Element> elements) {
    elements.stream()
        .filter(element ->
            StringUtils.hasText(element.getImageUrl()) && !MenuHelper.isUrl(element.getImageUrl()))
        .forEach(element -> ofNullable(s3.getUrl(menuBucket, element.getImageUrl()).toString())
            .ifPresent(url -> {
              log.info("Set image url: {}, element: {}", url, element.getName());
              element.setImageUrl(url);
            }));
  }

  @SneakyThrows
  public void uploadFile(MultipartFile file) {
    var metadata = new ObjectMetadata();
    metadata.setContentType("text/plain");
    metadata.setContentLength(file.getSize());
    s3.putObject(new PutObjectRequest(
        menuBucket,
        file.getOriginalFilename(),
        file.getInputStream(),
        metadata).withCannedAcl(CannedAccessControlList.PublicRead)
    );
  }

  public void uploadFiles(List<MultipartFile> files) {
    files.forEach(this::uploadFile);
  }
}
