package com.autocrypt.safe_no.quartz.controller;

import com.autocrypt.safe_no.quartz.controller.requestDTO.*;
import com.autocrypt.safe_no.quartz.service.QuartzSchedulingService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/scheduler")
@RequiredArgsConstructor
@ConditionalOnProperty(value = "quartz.enabled", havingValue = "true")
public class QuartzController {

    private final QuartzSchedulingService quartzSchedulingService;

    private static Class<? extends Job> getJobClass(String createCronJobRequest) throws ClassNotFoundException {
        return (Class<? extends Job>) Class.forName("com.autocrypt.safeno.quartz.jobs." + createCronJobRequest);
    }

    // 1. CRON Job 스케줄 등록
    @PostMapping("/schedule/cron")
    public ResponseEntity<String> scheduleCronJob(
            @RequestBody CreateCronJobRequest createCronJobRequest) throws Exception {

        Class<? extends Job> jobClass = getJobClass(createCronJobRequest.jobClassName());
        Date scheduledDate = quartzSchedulingService.scheduleCronJob(
                createCronJobRequest.jobName(),
                createCronJobRequest.jobGroup(),
                jobClass,
                createCronJobRequest.startAt(),
                createCronJobRequest.cronExpression(),
                createCronJobRequest.jobParams()
        );

        return ResponseEntity.ok("Cron Job [" + createCronJobRequest.jobName() + "] scheduled at " + scheduledDate);
    }

    // 2. SimpleTrigger로 한 번 실행되는 Job 스케줄 등록
    @PostMapping("/schedule/one-time")
    public ResponseEntity<String> scheduleOneTimeJob(
            @RequestBody CreateSimpleJobRequest createSimpleJobRequest) throws Exception {

        Class<? extends Job> jobClass = getJobClass(createSimpleJobRequest.jobClassName());
        Date scheduledDate = quartzSchedulingService.scheduleOneTimeJob(
                createSimpleJobRequest.jobName(),
                createSimpleJobRequest.jobGroup(),
                jobClass,
                createSimpleJobRequest.startAt(),
                createSimpleJobRequest.jobParams()
        );

        return ResponseEntity.ok("One-time Job [" + createSimpleJobRequest.jobName() + "] scheduled at " + scheduledDate);
    }

    // 3. CRON Job 스케줄 수정
    @PutMapping("/schedule/cron")
    public ResponseEntity<String> updateCronJobSchedule(
            @RequestBody UpdateCronJobRequest updateRequest) throws Exception {

        Date rescheduledDate = quartzSchedulingService.updateCronJobSchedule(
                updateRequest.jobName(),
                updateRequest.jobGroup(),
                updateRequest.newStartAt(),
                updateRequest.newCronExpression()
        );

        return ResponseEntity.ok("Cron Job [" + updateRequest.jobName() + "] rescheduled to " + rescheduledDate);
    }

    // 4. SimpleTrigger로 등록된 Job 스케줄 수정
    @PutMapping("/schedule/one-time")
    public ResponseEntity<String> updateSimpleTriggerJob(
            @RequestBody UpdateSimpleJobRequest updateRequest) throws Exception {

        Date rescheduledDate = quartzSchedulingService.updateSimpleTriggerJob(
                updateRequest.jobName(),
                updateRequest.jobGroup(),
                updateRequest.newStartAt()
        );

        return ResponseEntity.ok("One-time Job [" + updateRequest.jobName() + "] rescheduled to " + rescheduledDate);
    }

    // 5. 스케줄 삭제
    @DeleteMapping("/schedule/delete")
    public ResponseEntity<String> deleteJob(
            @RequestBody DeleteJobRequest deleteJobRequest) throws Exception {

        boolean deleted = quartzSchedulingService.deleteJob(
                deleteJobRequest.jobName(),
                deleteJobRequest.jobGroup()
        );

        if (deleted) {
            return ResponseEntity.ok("Job [" + deleteJobRequest.jobName() + "] deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Job [" + deleteJobRequest.jobName() + "] not found.");
        }
    }

    // 6. 스케줄 조회 - Job Detail 조회
    @GetMapping("/schedule/job-detail")
    public ResponseEntity<JobDetail> getJobDetail(@RequestParam String jobName, @RequestParam String jobGroup) throws Exception {
        JobDetail jobDetail = quartzSchedulingService.getJobDetail(jobName, jobGroup);
        return ResponseEntity.ok(jobDetail);
    }

    // 7. 스케줄 조회 - Trigger 조회
    @GetMapping("/schedule/trigger")
    public ResponseEntity<Trigger> getTrigger(@RequestParam String jobName, @RequestParam String jobGroup) throws Exception {
        Trigger trigger = quartzSchedulingService.getTrigger(jobName, jobGroup);
        return ResponseEntity.ok(trigger);
    }
}