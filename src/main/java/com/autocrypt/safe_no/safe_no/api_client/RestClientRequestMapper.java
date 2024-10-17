package com.autocrypt.safe_no.safe_no.api_client;

import com.autocrypt.safe_no.safe_no.config.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Map;

public interface RestClientRequestMapper {

    default String bodyToJsonString(Map<String, Object> body) {
        try {
            return new ObjectMapper().writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new CustomException("Error while serializing body to JSON");
        }
    }

    default HttpHeaders mapToHttpHeaders(Map<String, Object> headers) {
        // 헤더 설정 (Content-Type: application/json)
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // 추가적인 헤더가 있다면 추가
        if (headers != null) {
            headers.forEach((key, value) -> httpHeaders.add(key, value.toString()));
        }

        return httpHeaders;
    }

    default HttpEntity<String> makeHttpEntity(Map<String, Object> body, Map<String, Object> headers){
        return new HttpEntity<>(bodyToJsonString(body), mapToHttpHeaders(headers));
    }

    default JsonNode convertStringToJsonNode(String jsonResponse) {
        try {
            return new ObjectMapper().readTree(jsonResponse);
        } catch (JsonProcessingException e) {
            throw new CustomException("SKT response json parsing error");
        }
    }
}
