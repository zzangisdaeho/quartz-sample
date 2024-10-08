package com.autocrypt.safeno.quartz.service;

import com.autocrypt.safeno.quartz.jobs.SampleJob;
import org.junit.jupiter.api.Test;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"log_trace=on"})
class QuartzSchedulingServiceTest {

    @Autowired
    private QuartzSchedulingService quartzSchedulingService;

    @Test
    public void registerSimpleJob(){

        try {
            Date date = quartzSchedulingService.scheduleOneTimeJob("sample-simple2", "samples", SampleJob.class, ZonedDateTime.now().plusSeconds(10), Map.of("username", "daeho", "time", "10sec"));
            System.out.println("simple job start at : " + date);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void registerCronJob(){
        try {
            Date date = quartzSchedulingService.scheduleCronJob("sample-cron-0/3", "samples", SampleJob.class, ZonedDateTime.now(), "0/3 * * * * ?", Map.of("username", "daeho", "age", "3"));
            System.out.println("cron job start at : " + date);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteSchedule(){
        try {
            quartzSchedulingService.deleteJob("sample-cron-0/7", "samples");
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

}