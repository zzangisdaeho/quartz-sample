package com.autocrypt.safe_no.safe_no.api_client;

import com.autocrypt.logtracer.trace.annotation.LogTrace;
import com.autocrypt.safe_no.safe_no.api_client.dto.SafeNoClientReq;
import com.autocrypt.safe_no.safe_no.api_client.dto.SafeNoClientRes;

import java.util.Map;

@LogTrace
public abstract class AbstractSafeNoClient implements SafeNoClient {

    // 템플릿 메서드 구현 (외부에서 호출 가능)
    @Override
    public SafeNoClientRes createSafeNo(SafeNoClientReq req) {
        return createSafeNoInternal(req, makeRequestParam(req), makeBody(req), makeHeader(req));
    }

    @Override
    public SafeNoClientRes readSafeNo(SafeNoClientReq req) {
        return readSafeNoInternal(req, makeRequestParam(req), makeHeader(req));
    }

    @Override
    public SafeNoClientRes updateSafeNo(SafeNoClientReq req) {
        return updateSafeNoInternal(req,makeRequestParam(req), makeBody(req), makeHeader(req));
    }

    @Override
    public SafeNoClientRes deleteSafeNo(SafeNoClientReq req) {
        return deleteSafeNoInternal(req, makeRequestParam(req), makeHeader(req));
    }

    // 추상 메서드로 각 클라이언트에서 구현해야 하는 내부 CRUD 메서드 정의
    protected abstract SafeNoClientRes createSafeNoInternal(SafeNoClientReq req,
                                                            Map<String, Object> requestParams,
                                                            Map<String, Object> body,
                                                            Map<String, Object> headers);

    protected abstract SafeNoClientRes readSafeNoInternal(SafeNoClientReq req,
                                                          Map<String, Object> requestParams,
                                                          Map<String, Object> headers);

    protected abstract SafeNoClientRes updateSafeNoInternal(SafeNoClientReq req,
                                                            Map<String, Object> requestParams,
                                                            Map<String, Object> body,
                                                            Map<String, Object> headers);

    protected abstract SafeNoClientRes deleteSafeNoInternal(SafeNoClientReq req,
                                                            Map<String, Object> requestParams,
                                                            Map<String, Object> headers);
}