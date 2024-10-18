package com.autocrypt.safe_no.safe_no.controller;

import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import com.autocrypt.safe_no.safe_no.controller.dto.req.CreateSafeNoReq;
import com.autocrypt.safe_no.safe_no.controller.dto.res.CreateSafeNoRes;
import com.autocrypt.safe_no.safe_no.controller.dto.res.GetSafeNoRes;
import com.autocrypt.safe_no.safe_no.service.SafeNoService;
import com.autocrypt.safe_no.safe_no.util.SafeNoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/v1/drives")
@RequiredArgsConstructor
public class SafeNoController {

    private final SafeNoService safeNoService;

    //spring에서 주관하는 controller에 들어오는 telNo에 대해서 숫자만 남기도록 설정
    @InitBinder("telNo")
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(SafeNoUtil.getTelNoNumberOnly(text));
            }
        });
    }

    @PostMapping("/{driveId}")
    public ResponseEntity<CreateSafeNoRes> createSafeNo(
        @PathVariable("driveId") String driveId,
        @RequestHeader("autocrypt-service-id") SafeNoProperties.ServiceEnum serviceId,
        @RequestBody @Validated CreateSafeNoReq req) {

        CreateSafeNoRes response = safeNoService.createSafeNo(driveId, req.getTelNo(), serviceId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{driveId}/passengers/{telNo}")
    public ResponseEntity<GetSafeNoRes> getSafeNo(
        @PathVariable("driveId") String driveId,
        @PathVariable("telNo") String telNo,
        @RequestHeader("autocrypt-service-id") SafeNoProperties.ServiceEnum serviceId) {
        GetSafeNoRes response = safeNoService.getSafeNo(driveId, telNo, serviceId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{driveId}/finish")
    public ResponseEntity<Object> finishDriving(
            @PathVariable("driveId") String driveId) {
        safeNoService.updateSafeNoDeleteTime(driveId);
        return ResponseEntity.ok(null);
    }

}