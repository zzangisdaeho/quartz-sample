package com.autocrypt.safe_no.safe_no.controller;

import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import com.autocrypt.safe_no.safe_no.controller.dto.req.CreateSafeNoReq;
import com.autocrypt.safe_no.safe_no.controller.dto.req.FinishDriveReq;
import com.autocrypt.safe_no.safe_no.controller.dto.res.CreateSafeNoRes;
import com.autocrypt.safe_no.safe_no.controller.dto.res.GetSafeNoRes;
import com.autocrypt.safe_no.safe_no.service.SafeNoService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.beans.PropertyEditorSupport;

@RestController
@RequestMapping("/backend/v1/drives")
@RequiredArgsConstructor
public class SafeNoController {

    private final SafeNoService safeNoService;

    //controller에 들어오는 telNo에 대해서 숫자만 남기도록 설정
    @InitBinder("telNo")
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text.replaceAll("\\D", "")); // 숫자가 아닌 문자를 제거
            }
        });
    }


    @PostMapping("/{bookingId}")
    public ResponseEntity<CreateSafeNoRes> createSafeNo(
        @PathVariable("bookingId") String bookingId,
        @RequestBody @Validated CreateSafeNoReq req) {

        CreateSafeNoRes response = safeNoService.createSafeNo(bookingId, req);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{bookingId}/passengers/{telNo}")
    public ResponseEntity<GetSafeNoRes> getSafeNo(
        @PathVariable("bookingId") String bookingId,
        @PathVariable("telNo") String telNo,
        @RequestParam("serviceId") SafeNoProperties.ServiceEnum serviceId) {
        GetSafeNoRes response = safeNoService.getSafeNo(bookingId, telNo, serviceId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{bookingId}/finish")
    public ResponseEntity<Object> finishDriving(
            @PathVariable("bookingId") String bookingId) {
        safeNoService.updateSafeNoDeleteTime(bookingId);
        return ResponseEntity.ok(null);
    }

}