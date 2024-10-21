package com.autocrypt.safe_no.safe_no.api_client.listener;

import com.autocrypt.safe_no.safe_no.api_client.SafeNoClient;
import com.autocrypt.safe_no.safe_no.api_client.dto.SafeNoClientReq;
import com.autocrypt.safe_no.safe_no.api_client.dto.SafeNoClientRes;
import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import com.autocrypt.safe_no.safe_no.service.event.SafeNoFailEvent;
import com.autocrypt.safe_no.safe_no.util.SafeNoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiClientEventListener {

    private final Map<SafeNoProperties.ProviderEnum, SafeNoClient> safeNoClientMap;

    // create한 safeNo를 rollback하기 위함
    @Async
    @EventListener
    public CompletableFuture<SafeNoClientRes> handleCreateEvent(@Validated SafeNoFailEvent safeNoFailEvent) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                SafeNoClient safenoClient = safeNoClientMap.get(SafeNoUtil.getProviderEnum(safeNoFailEvent.getServiceEnum()));
                return safenoClient.deleteSafeNo(SafeNoClientReq.builder().safeNo(safeNoFailEvent.getSafeNo()).serviceId(safeNoFailEvent.getServiceEnum()).build());
            } catch (Exception e) {
                throw new RuntimeException("Fail delete driveId: " + safeNoFailEvent.getDriveId() + "safeNo: " + safeNoFailEvent.getSafeNo(), e);
            }
        }).exceptionally(ex -> {
            log.error("Error during safeno rollback. driveId : {}, safeNo : {}", safeNoFailEvent.getDriveId(), safeNoFailEvent.getSafeNo(), ex);
            return null;
        });
    }
}
