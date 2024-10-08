package com.autocrypt.safeno.quartz.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "quartz.enabled", havingValue = "false", matchIfMissing = true)
@EnableAutoConfiguration(exclude = QuartzAutoConfiguration.class)
@Slf4j
public class CustomQuartzConfig {
    // 이 클래스는 quartz.enabled=true 일 때만 QuartzAutoConfiguration을 사용

    @PostConstruct
    public void init() {
        log.warn("Quartz disabled");
    }
}