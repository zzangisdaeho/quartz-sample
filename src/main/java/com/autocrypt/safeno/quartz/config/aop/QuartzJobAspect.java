package com.autocrypt.safeno.quartz.config.aop;

import com.autocrypt.safeno.quartz.config.annotation.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class QuartzJobAspect {

    private final Scheduler scheduler;

    // @CheckQuartzJobExist: Job이 이미 존재하는지 체크
    @Before("@annotation(com.autocrypt.safeno.quartz.config.annotation.CheckQuartzJobExist) && args(jobName, jobGroup, ..)")
    public void checkJobExists(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            throw new SchedulerException("Job with this name and group already exists: " + jobName + ", " + jobGroup);
        }
    }

    // @CheckQuartzJobNotExist: Job이 존재하지 않을 경우 체크
    @Before("@annotation(com.autocrypt.safeno.quartz.config.annotation.CheckQuartzJobNotExist) && args(jobName, jobGroup, ..)")
    public void checkJobNotExists(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        if (!scheduler.checkExists(jobKey)) {
            throw new SchedulerException("Job with this name and group does not exist: " + jobName + ", " + jobGroup);
        }
    }

    @PostConstruct
    public void checkRegister(){
        log.info("QuartzJobAspect applied!");
    }
}