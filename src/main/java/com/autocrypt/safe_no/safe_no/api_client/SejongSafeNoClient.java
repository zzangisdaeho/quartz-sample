package com.autocrypt.safe_no.safe_no.api_client;

import com.autocrypt.logtracer.trace.annotation.LogTrace;
import com.autocrypt.logtracer.trace.logtrace.ThreadLocalLogTrace;
import com.autocrypt.safe_no.safe_no.api_client.dto.SafeNoClientReq;
import com.autocrypt.safe_no.safe_no.api_client.dto.SafeNoClientRes;
import com.autocrypt.safe_no.safe_no.api_client.exception.SejongSafeNoError;
import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import com.autocrypt.safe_no.safe_no.util.SafeNoUtil;
import com.autocrypt.safe_no.safe_no.util.TimeUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Map;

@Component("sejongSafeNoClient")
@Slf4j
@LogTrace
public class SejongSafeNoClient extends AbstractSafeNoClient implements RestClientRequestMapper {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final SafeNoProperties.Provider provider;

    public SejongSafeNoClient(RestTemplate restTemplate, ObjectMapper objectMapper, SafeNoProperties safeNoProperties) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.provider = safeNoProperties.getProvider().get(SafeNoProperties.ProviderEnum.SEJONG);
    }

    @Override
    protected SafeNoClientRes createSafeNoInternal(SafeNoClientReq req, Map<String, Object> requestParams, Map<String, Object> body, Map<String, Object> headers) {
        String baseUrl = provider.getDomain()
                + "/050biz/v1/service/assign";

        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).build().toUri();

        ResponseEntity<String> response = getStringResponseEntity(HttpMethod.POST, uri, makeHttpEntity(body, headers));
        // HTTP 호출 JSON parsing
        String jsonResponse = response.getBody();
        JsonNode jsonNode = convertStringToJsonNode(jsonResponse);
        Map<String, Object> others = objectMapper.convertValue(jsonNode, Map.class);

        // JSON 응답을 파싱하여 SafenoClientRes에 매핑
        return SafeNoClientRes.builder().telNo(req.getTelNo()).safeNo(jsonNode.get("data").asText()).others(others).build();
    }

    @Override
    protected SafeNoClientRes readSafeNoInternal(SafeNoClientReq req, Map<String, Object> requestParams, Map<String, Object> headers) {
        String baseUrl = provider.getDomain()
                + "/050biz/v1/service/" + req.getSafeNo();
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).build().toUri();

        ResponseEntity<String> response = getStringResponseEntity(HttpMethod.GET, uri, makeHttpEntity(null, headers));
        // HTTP 호출 JSON parsing
        String jsonResponse = response.getBody();
        JsonNode jsonNode = convertStringToJsonNode(jsonResponse);

        // JSON 응답을 파싱하여 SafenoClientRes에 매핑
        JsonNode data = jsonNode.get("data");
        String rcvNo1 = data.get("rcvNo1").asText();
        Map<String, Object> others = objectMapper.convertValue(jsonNode, Map.class);

        return SafeNoClientRes.builder().telNo(rcvNo1).safeNo(req.getSafeNo()).others(others).build();
    }

    @Override
    protected SafeNoClientRes updateSafeNoInternal(SafeNoClientReq req, Map<String, Object> requestParams, Map<String, Object> body, Map<String, Object> headers) {
        return null;
    }

    @Override
    protected SafeNoClientRes deleteSafeNoInternal(SafeNoClientReq req, Map<String, Object> requestParams, Map<String, Object> headers) {
        String baseUrl = provider.getDomain()
                + "/050biz/v1/service/clear/" + req.getSafeNo();
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).build().toUri();

        ResponseEntity<String> response = getStringResponseEntity(HttpMethod.POST, uri, makeHttpEntity(null, headers));
        // HTTP 호출 JSON parsing
        String jsonResponse = response.getBody();
        JsonNode jsonNode = convertStringToJsonNode(jsonResponse);

        // JSON 응답을 파싱하여 SafenoClientRes에 매핑
        Map<String, Object> others = objectMapper.convertValue(jsonNode, Map.class);

        return SafeNoClientRes.builder().others(others).build();
    }

    @Override
    public Map<String, Object> makeRequestParam(SafeNoClientReq req) {
        return Map.of();
    }

    @Override
    public Map<String, Object> makeHeader(SafeNoClientReq req) {
        return Map.of("Authorization", provider.getDetails().get("authorization"));
    }

    @Override
    public Map<String, Object> makeBody(SafeNoClientReq req) {
        ZonedDateTime endTime = TimeUtil.switchZoneToKorea(ZonedDateTime.now().plus(SafeNoUtil.getServiceProperty(req.getServiceId()).getDeleteTime()));
        return Map.of(
                "channelId", provider.getDetails().get("channel-id"),
                "rcvNo1", req.getTelNo(),
                "endDay", TimeUtil.formatZonedDateTimeForSejong(endTime),
                "agingDay", TimeUtil.formatZonedDateTimeForSejong(endTime.plusDays(1))
        );
    }

    @Override
    public SafeNoProperties.ProviderEnum getProvider() {
        return SafeNoProperties.ProviderEnum.SEJONG;
    }

    private ResponseEntity<String> getStringResponseEntity(HttpMethod httpMethod, URI uri, HttpEntity<String> httpEntity) {
        ResponseEntity<String> response;
        try {
            log.debug("[{}]sejong request. \nuri : {}. \nhttpEntity: {}", ThreadLocalLogTrace.currentId(), uri.toString(), httpEntity);
            response = restTemplate.exchange(uri, httpMethod, httpEntity, String.class);
            log.debug("[{}]sejong response: {}", ThreadLocalLogTrace.currentId(), response.getBody());
        } catch (HttpClientErrorException e) {
            log.error("sejong safeNo create fail by 400 status. {} ", e.getMessage(), e);
            SejongSafeNoError.SejongSafeNoErrorCode code = mappingErrorCode(e);
            if(code == SejongSafeNoError.SejongSafeNoErrorCode.UNKNOWN) code = SejongSafeNoError.SejongSafeNoErrorCode.CLIENT_ERROR;
            throw new SejongSafeNoError(code.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, code);
        } catch (HttpServerErrorException e) {
            log.error("sejong safeNo create fail by 500 status. {} ", e.getMessage(), e);
            SejongSafeNoError.SejongSafeNoErrorCode code = mappingErrorCode(e);
            if(code == SejongSafeNoError.SejongSafeNoErrorCode.UNKNOWN) code = SejongSafeNoError.SejongSafeNoErrorCode.SEJONG_SERVER_ERROR;
            throw new SejongSafeNoError(code.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, code);
        }
        return response;
    }

    private SejongSafeNoError.SejongSafeNoErrorCode mappingErrorCode(HttpStatusCodeException e) {
        JsonNode jsonNode = convertStringToJsonNode(e.getMessage());
        String code = jsonNode.get("code").asText();
        String message = jsonNode.get("message").asText();
        return SejongSafeNoError.SejongSafeNoErrorCode.fromCodeAndMessage(code, message);
    }
}
