package com.autocrypt.safe_no.safe_no.api_client;

import com.autocrypt.logtracer.trace.annotation.LogTrace;
import com.autocrypt.logtracer.trace.logtrace.ThreadLocalLogTrace;
import com.autocrypt.safe_no.safe_no.api_client.dto.SafeNoClientReq;
import com.autocrypt.safe_no.safe_no.api_client.dto.SafeNoClientRes;
import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import com.autocrypt.safe_no.safe_no.api_client.exception.SKTSafeNoError;
import com.autocrypt.safe_no.safe_no.util.TimeUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component("sktSafeNoClient")
@LogTrace
@Slf4j
public class SKTSafeNoClient extends AbstractSafeNoClient implements RestClientRequestMapper {


    private final RestTemplate restTemplate;
    private final SafeNoProperties safeNoProperties;

    private final SafeNoProperties.ProviderEnum providerEnum = SafeNoProperties.ProviderEnum.SKT;
    private final SafeNoProperties.Provider provider;
    private final ObjectMapper objectMapper;

    public SKTSafeNoClient(RestTemplate restTemplate, SafeNoProperties safeNoProperties, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.safeNoProperties = safeNoProperties;
        this.provider = safeNoProperties.getProvider().get(providerEnum);
        this.objectMapper = objectMapper;
    }

    @Override
    public SafeNoClientRes createSafeNoInternal(SafeNoClientReq req,
                                                Map<String, Object> requestParams,
                                                Map<String, Object> body,
                                                Map<String, Object> headers) {

        if (requestParams == null) requestParams = Collections.EMPTY_MAP;

        // 기본 URL
        String baseUrl = provider.getDomain()
                + "/skt_safen/safen/MappingAdd";

        // URI 빌더를 사용해 쿼리 파라미터 추가
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("group_code", provider.getDetails().getOrDefault("group-code", "autocrypt"))
                .queryParam("customer_code", provider.getDetails().getOrDefault("customer-code", "1308"))
                .queryParam("send_date", TimeUtil.formatZonedDateTime(TimeUtil.switchZoneToKorea(ZonedDateTime.now())))
                .queryParam("tel_no", req.getTelNo())
                .queryParam("new_flag", requestParams.getOrDefault("new_flag", "Y"))
                .build()
                .toUri();

        ResponseEntity<String> response = getStringResponseEntity(uri);
        // HTTP 호출 JSON parsing
        String jsonResponse = response.getBody();
        JsonNode jsonNode = convertStringToJsonNode(jsonResponse);
        checkResponseCorrect(jsonNode, uri, null);

        // JSON 응답을 파싱하여 SafenoClientRes에 매핑
        return parseJsonToSafenoClientRes(jsonNode);

    }

    private ResponseEntity<String> getStringResponseEntity(URI uri) {
        ResponseEntity<String> response;
        try{
            log.debug("[{}]skt request: {}", ThreadLocalLogTrace.traceIdHolder.get().getId(), uri.toString());
            response = restTemplate.getForEntity(uri, String.class);
            log.debug("[{}]skt response: {}", ThreadLocalLogTrace.traceIdHolder.get().getId(), response.getBody());
        }catch (HttpClientErrorException e){
            log.error("skt safeNo create fail by 400 status. {} ", e.getMessage(), e);
            throw new SKTSafeNoError(SKTSafeNoError.SKTSafeNoErrorCode.CLIENT_ERROR.getMean(), SKTSafeNoError.SKTSafeNoErrorCode.CLIENT_ERROR);
        }catch (HttpServerErrorException e){
            log.error("skt safeNo create fail by 500 status. {} ", e.getMessage(), e);
            throw new SKTSafeNoError(SKTSafeNoError.SKTSafeNoErrorCode.SKT_SERVER_ERROR.getMean(), SKTSafeNoError.SKTSafeNoErrorCode.SKT_SERVER_ERROR);
        }
        return response;
    }

    @Override
    public SafeNoClientRes readSafeNoInternal(SafeNoClientReq req,
                                              Map<String, Object> requestParams,
                                              Map<String, Object> headers) {

        // 기본 URL
        String baseUrl = provider.getDomain()
                + "/skt_safen/safen/MappingQry";

        // URI 빌더를 사용해 쿼리 파라미터 추가
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("group_code", provider.getDetails().getOrDefault("group-code", "autocrypt"))
                .queryParam("customer_code", provider.getDetails().getOrDefault("customer-code", "1308"))
                .queryParam("send_date", TimeUtil.formatZonedDateTime(TimeUtil.switchZoneToKorea(ZonedDateTime.now())))
                .queryParam("qry_no", req.getSafeNo())
                .queryParam("qry_flag", requestParams.getOrDefault("qry_flag", "S"))
                .build()
                .toUri();

        // HTTP 호출 및 JSON 응답 받기
        ResponseEntity<String> response = getStringResponseEntity(uri);

        String jsonResponse = response.getBody();
        JsonNode jsonNode = convertStringToJsonNode(jsonResponse);
        checkResponseCorrect(jsonNode, uri, null);

        // JSON 응답을 파싱하여 SafenoClientRes에 매핑
        return parseJsonToSafenoClientRes(jsonNode);
    }

    @Override
    public SafeNoClientRes updateSafeNoInternal(SafeNoClientReq req,
                                                Map<String, Object> requestParams,
                                                Map<String, Object> body,
                                                Map<String, Object> headers) {
        // SKT의 경우 Update 작업이 없을 수 있음, 필요 시 구현
        throw new UnsupportedOperationException("Update operation is not supported for SKT");
    }

    @Override
    public SafeNoClientRes deleteSafeNoInternal(SafeNoClientReq req,
                                                Map<String, Object> requestParams,
                                                Map<String, Object> headers) {

        // 기본 URL
        String baseUrl = provider.getDomain()
                + "/skt_safen/safen/MappingDel";

        // URI 빌더를 사용해 쿼리 파라미터 추가
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("group_code", provider.getDetails().getOrDefault("group-code", "autocrypt"))
                .queryParam("customer_code", provider.getDetails().getOrDefault("customer-code", "1308"))
                .queryParam("send_date", TimeUtil.formatZonedDateTime(TimeUtil.switchZoneToKorea(ZonedDateTime.now())))
                .queryParam("safen_no", req.getSafeNo())
                .build()
                .toUri();

        // HTTP 호출 및 JSON 응답 받기
        ResponseEntity<String> response = getStringResponseEntity(uri);
        String jsonResponse = response.getBody();
        JsonNode jsonNode = convertStringToJsonNode(jsonResponse);
        checkResponseCorrect(jsonNode, uri, null);

        // JSON 응답을 파싱하여 SafenoClientRes에 매핑
        return parseJsonToSafenoClientRes(jsonNode);

    }

    @Override
    public SafeNoProperties.ProviderEnum getProvider() {
        return SafeNoProperties.ProviderEnum.SKT;
    }

    @Override
    public Map<String, Object> makeRequestParam(SafeNoClientReq req) {
        HashMap<String, Object> requestParams = new HashMap<>();
        if(req.getOthers().get("qry_flag") != null) requestParams.put("qry_flag", req.getOthers().get("qry_flag"));
        return requestParams;
    }

    @Override
    public Map<String, Object> makeHeader(SafeNoClientReq req) {
        return Map.of();
    }

    @Override
    public Map<String, Object> makeBody(SafeNoClientReq req) {
        return Map.of();
    }

    private void checkResponseCorrect(JsonNode jsonNode, URI uri, HttpEntity<String> httpEntity) {
        String result = jsonNode.get("result").asText();

        // 응답이 성공 코드가 아닌 경우 각 코드별로 CustomException을 던짐
        if (!result.equalsIgnoreCase("0000")) {
            log.error("skt safeno error occur.\n uri: {} \n request: {}\n response: {}", uri, httpEntity, jsonNode.toPrettyString());

            SKTSafeNoError.SKTSafeNoErrorCode errorCode = SKTSafeNoError.SKTSafeNoErrorCode.fromCode(result);

            // 적절한 에러 메시지를 던짐
            throw new SKTSafeNoError(errorCode.getMean(), errorCode);
        }
    }

    // JSON 응답을 SafenoClientRes로 매핑하는 메서드
    private SafeNoClientRes parseJsonToSafenoClientRes(JsonNode jsonNode) {

        // 기본 필드 매핑
        String telNo = jsonNode.get("tel_no") != null ? jsonNode.get("tel_no").asText() : null;
        String safeNo = jsonNode.get("safen_no") != null ? jsonNode.get("safen_no").asText() : null;

        // 나머지 값들을 others에 추가
        Map<String, Object> others = objectMapper.convertValue(jsonNode, Map.class);

        // SafenoClientRes 객체 생성 및 반환
        return SafeNoClientRes.builder()
                .telNo(telNo)
                .safeNo(safeNo)
                .others(others)
                .build();
    }

}