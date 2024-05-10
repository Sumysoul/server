package com.jdum.commerce.sumysoul;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

@Slf4j
public class StreamLambdaHandler implements RequestStreamHandler {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private static final SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

  static {
    try {
      handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(ServerlessApplication.class);
      // If you are using HTTP APIs with the version 2.0 of the proxy model, use the getHttpApiV2ProxyHandler
      // method: handler = SpringBootLambdaContainerHandler.getHttpApiV2ProxyHandler(Application.class);
    } catch (ContainerInitializationException e) {
      throw new RuntimeException("Could not initialize Spring Boot application", e);
    }
  }

  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
      throws IOException {
    handler.proxyStream(inputStream, outputStream, context);
  }

  //switch to the next implementation if scheduled event should trigger lambda
  /*
  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
      throws IOException {
    var byteArray = IOUtils.toByteArray(inputStream);
    var rootNode = objectMapper.readTree(new ByteArrayInputStream(byteArray));
    if (isScheduledEvent(rootNode)) {
      log.info("Scheduled event triggered: {}", rootNode);
    } else {
      handler.proxyStream(new ByteArrayInputStream(byteArray), outputStream, context);
    }
  }

  private boolean isScheduledEvent(JsonNode eventNode) {
    return eventNode.has("source")
        && "aws.events".equals(eventNode.get("source").asText())
        && eventNode.has("detail-type")
        && eventNode.get("detail-type").asText().startsWith("Scheduled");
  }
   */
}
