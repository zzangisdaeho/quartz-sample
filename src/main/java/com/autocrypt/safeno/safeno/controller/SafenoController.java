package com.autocrypt.safeno.safeno.controller;

import com.autocrypt.safeno.quartz.jobs.SampleSimpleJob;
import com.autocrypt.safeno.quartz.service.QuartzSchedulingEventListener;
import com.autocrypt.safeno.safeno.config.SafenoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@RestController
@RequiredArgsConstructor
public class SafenoController {

    private final SafenoProperties safenoProperties;

    private final ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("/test")
    public void test(){
        safenoProperties.toString();
    }

    @GetMapping("/event")
    @Transactional
    public String testEvent(){
        applicationEventPublisher.publishEvent(
                QuartzSchedulingEventListener.QuartzJobCreateEvent.builder()
                        .startAt(ZonedDateTime.now().plusSeconds(10))
                        .jobName("testJob1")
                        .jobGroup("testGroup")
                        .jobClass(SampleSimpleJob.class)
                        .build()

        );
        return "갔니?";
    }
}
