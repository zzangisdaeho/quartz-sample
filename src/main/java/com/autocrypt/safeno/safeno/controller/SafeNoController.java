package com.autocrypt.safeno.safeno.controller;

import com.autocrypt.safeno.safeno.controller.dto.req.CreateSafeNoReq;
import com.autocrypt.safeno.safeno.controller.dto.req.DeleteSafeNoReq;
import com.autocrypt.safeno.safeno.controller.dto.req.UpdateExpiredDateReq;
import com.autocrypt.safeno.safeno.controller.dto.res.CreateSafeNoRes;
import com.autocrypt.safeno.safeno.controller.dto.res.DeleteSafeNoRes;
import com.autocrypt.safeno.safeno.controller.dto.res.GetSafeNoRes;
import com.autocrypt.safeno.safeno.controller.dto.res.UpdateExpiredDateRes;
import com.autocrypt.safeno.safeno.service.SafeNoService;
import com.autocrypt.safeno.safeno.util.SafeNoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/backend/v1")
@RequiredArgsConstructor
public class SafeNoController {

    private final SafeNoService safeNoService;


    @PostMapping("/{telNo}")
    public ResponseEntity<CreateSafeNoRes> createSafeNo(
        @PathVariable("telNo") String telNo, 
        @RequestBody CreateSafeNoReq req) {
        CreateSafeNoRes response = safeNoService.createSafeNo(SafeNoUtil.getTelNoNumberOnly(telNo), req);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{telNo}")
    public ResponseEntity<GetSafeNoRes> getSafeNo(
        @PathVariable("telNo") String telNo,
        @RequestParam("bookingId") String bookingId) {
//        GetSafeNoRes response = safeNoService.getSafeNo(telNo, bookingId);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{telNo}")
    public ResponseEntity<DeleteSafeNoRes> deleteSafeNoList(
        @PathVariable("telNo") String telNo,
        @RequestBody DeleteSafeNoReq req) {
//        DeleteSafeNoRes response = safeNoService.deleteSafeNo(telNo, req);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/expired-date")
    public ResponseEntity<UpdateExpiredDateRes> updateExpiredDate(
        @RequestBody UpdateExpiredDateReq req) {
//        UpdateExpiredDateRes response = safeNoService.updateExpiredDate(req);
        return ResponseEntity.ok(null);
    }
}