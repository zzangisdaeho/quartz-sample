package com.autocrypt.safeno.safeno.controller;

import com.autocrypt.safeno.safeno.config.SafenoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SafenoController {

    private final SafenoProperties safenoProperties;

    @GetMapping("/test")
    public void test(){
        safenoProperties.toString();
    }
}
