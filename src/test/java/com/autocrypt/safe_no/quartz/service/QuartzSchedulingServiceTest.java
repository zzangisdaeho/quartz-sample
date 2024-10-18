package com.autocrypt.safe_no.quartz.service;

import com.autocrypt.safe_no.quartz.jobs.SampleCronJob;
import com.autocrypt.safe_no.quartz.jobs.SampleSimpleJob;
import org.junit.jupiter.api.Test;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@SpringBootTest
@TestPropertySource(properties = {
        "quartz.enabled=true"
})
@ActiveProfiles("test")
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

        applicationEventPublisher.publishEvent(
                QuartzSchedulingEventListener.QuartzJobCreateEvent.builder()
                        .startAt(ZonedDateTime.now().plusSeconds(10))
                        .jobName("testJob1")
                        .jobGroup("testGroup")
                        .jobClass(SampleSimpleJob.class)
                        .build()

        );
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
            quartzSchedulingService.deleteJob("safe-no-delete:abc", "safe-no-delete");
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