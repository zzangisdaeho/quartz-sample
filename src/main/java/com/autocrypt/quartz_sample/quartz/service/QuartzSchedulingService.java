package com.autocrypt.quartz_sample.quartz.service;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuartzSchedulingService {

    private static final String CRON_TRIGGER_PREFIX = "-Cron-Trigger";
    private static final String ONETIME_TRIGGER_PREFIX = "-Simple-Trigger";

    private final Scheduler scheduler;

    // 1. 스케줄 등록 (Create) - CRON Job
    public Date scheduleCronJob(String jobName, String jobGroup, Class<? extends Job> jobClass, ZonedDateTime startAt, String cronExpression, @Nullable Map<String, Object> jobParams) throws SchedulerException {
        JobDetail jobDetail = getJobDetail(jobName, jobGroup, jobClass, jobParams);

        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName + CRON_TRIGGER_PREFIX, jobGroup)
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)
                        .inTimeZone(TimeZone.getTimeZone(startAt.getZone())))
                .build();

        Date scheduleDate = scheduler.scheduleJob(jobDetail, cronTrigger);
        log.info("Cron Job [{}] in group [{}] scheduled successfully to run at [{}]", jobName, jobGroup, scheduleDate);
        return scheduleDate;
    }

    // 2. 스케줄 등록 (특정 시간에 한 번만 실행) - SimpleTrigger
    public Date scheduleOneTimeJob(String jobName, String jobGroup, Class<? extends Job> jobClass, ZonedDateTime startAt, @Nullable Map<String, Object> jobParams) throws SchedulerException {
        JobDetail jobDetail = getJobDetail(jobName, jobGroup, jobClass, jobParams);

        SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName + ONETIME_TRIGGER_PREFIX, jobGroup)
                .startAt(Date.from(startAt.toInstant()))  // 지정된 시간에 실행
                .forJob(jobDetail)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .build();

        Date scheduleDate = scheduler.scheduleJob(jobDetail, simpleTrigger);
        log.info("One-time Job [{}] in group [{}] scheduled successfully to run at [{}]", jobName, jobGroup, scheduleDate);
        return scheduleDate;
    }

    // 3. 스케줄 수정 (Update) - CRON Job 수정
    public Date updateCronJobSchedule(String jobName, String jobGroup, ZonedDateTime newStartAt, String newCronExpression) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(jobName + CRON_TRIGGER_PREFIX, jobGroup);
        CronTrigger newTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .startAt(Date.from(newStartAt.toInstant()))
                .withSchedule(CronScheduleBuilder.cronSchedule(newCronExpression))
                .build();

        Date rescheduleDate = scheduler.rescheduleJob(triggerKey, newTrigger);
        log.info("Cron Job [{}] in group [{}] rescheduled successfully to run at [{}]", jobName, jobGroup, rescheduleDate);
        return rescheduleDate;
    }

    // 4. 스케줄 수정 (Update) - SimpleTrigger 수정
    public Date updateSimpleTriggerJob(String jobName, String jobGroup, ZonedDateTime newStartAt) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(jobName + ONETIME_TRIGGER_PREFIX, jobGroup);

        SimpleTrigger newTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .startAt(Date.from(newStartAt.toInstant())) // 새로운 실행 시간 설정
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .build();

        Date rescheduleDate = scheduler.rescheduleJob(triggerKey, newTrigger);
        log.info("One-time Job [{}] in group [{}] rescheduled successfully to run at [{}]", jobName, jobGroup, rescheduleDate);
        return rescheduleDate;
    }

    // 5. 스케줄 삭제 (Delete)
    public boolean deleteJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        boolean deleted = scheduler.deleteJob(jobKey);
        if (deleted) {
            log.info("Job [{}] in group [{}] deleted successfully", jobName, jobGroup);
        } else {
            log.warn("Failed to delete Job [{}] in group [{}]", jobName, jobGroup);
        }
        return deleted;
    }

    // 6. 스케줄 조회 (Read)
    public JobDetail getJobDetail(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        return scheduler.getJobDetail(jobKey);
    }

    // 7. 트리거 조회
    public Trigger getTrigger(String jobName, String jobGroup, boolean isOneTime) throws SchedulerException {
        String triggerName = isOneTime ? jobName+ONETIME_TRIGGER_PREFIX : jobName+CRON_TRIGGER_PREFIX;
        TriggerKey triggerKey = new TriggerKey(triggerName, jobGroup);
        return scheduler.getTrigger(triggerKey);
    }

    // JobDetail 생성 메서드
    private static JobDetail getJobDetail(String jobName, String jobGroup, Class<? extends Job> jobClass, Map<String, Object> jobParams) {
        return JobBuilder.newJob(jobClass)
                .withIdentity(jobName, jobGroup)
                .setJobData(jobParams != null? new JobDataMap(jobParams) : new JobDataMap())
                .build();
    }
}