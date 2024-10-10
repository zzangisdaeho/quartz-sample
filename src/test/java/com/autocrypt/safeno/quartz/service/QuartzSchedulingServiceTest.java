package com.autocrypt.safeno.quartz.service;

import com.autocrypt.safeno.quartz.jobs.SampleCronJob;
import com.autocrypt.safeno.quartz.jobs.SampleSimpleJob;
import org.junit.jupiter.api.Test;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@SpringBootTest
@TestPropertySource(properties = {
        "log_trace=on",
        "quartz.enabled=true"
})
class QuartzSchedulingServiceTest {

    @Autowired
    private QuartzSchedulingService quartzSchedulingService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    public void registerSimpleJob(){

        try {
            Date date = quartzSchedulingService.scheduleOneTimeJob("sample-simple2", "samples", SampleSimpleJob.class, ZonedDateTime.now().plusSeconds(10), null);
            System.out.println("simple job start at : " + date);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Test
    public void registerSimpleJobByEvent(){

        applicationEventPublisher.publishEvent(new QuartzSchedulingEventListener.QuartzJobEventDTO("sample-simple3", "samples", SampleSimpleJob.class, ZonedDateTime.now().plusSeconds(10), null, null, QuartzSchedulingEventListener.QuartzJobEventDTO.QuartzJobEventType.CREATE, QuartzSchedulingEventListener.QuartzJobEventDTO.QuartzJobType.SIMPLE));
    }

    @Test
    public void registerCronJob(){
        try {
            Date date = quartzSchedulingService.scheduleCronJob("sample-cron-0/3", "samples", SampleCronJob.class, ZonedDateTime.now(), "0/3 * * * * ?", Map.of("username", "daeho", "age", "3"));
            System.out.println("cron job start at : " + date);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteSimpleSchedule(){
        try {
            quartzSchedulingService.deleteJob("sample-simple2", "samples");
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteCronSchedule(){
        try {
            quartzSchedulingService.deleteJob("sample-cron-0/3", "samples");
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

}