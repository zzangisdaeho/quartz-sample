package com.autocrypt.safeno.quartz.service;

import com.autocrypt.logtracer.trace.annotation.LogTrace;
import jakarta.annotation.Nullable;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@LogTrace
@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(value = "quartz.enabled", havingValue = "true")
@EnableAsync
public class QuartzSchedulingEventListener {

    private final QuartzSchedulingService quartzSchedulingService;

//    @Value(value = "${test.error:false}")
//    private boolean testMode;

    // 1. CREATE 이벤트 처리 (Cron & Simple)
    @Async
    @EventListener
    public CompletableFuture<ZonedDateTime> handleCreateEvent(QuartzJobCreateEvent event) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Date startTime;
                if (event.getCronExpression() != null) {
                    // CRON Job 생성
                    startTime = quartzSchedulingService.scheduleCronJob(
                            event.getJobName(),
                            event.getJobGroup(),
                            event.getJobClass().asSubclass(Job.class),
                            event.getStartAt(),
                            event.getCronExpression(),
                            event.getJobParams()
                    );
                } else {
                    // Simple Job 생성
                    startTime = quartzSchedulingService.scheduleOneTimeJob(
                            event.getJobName(),
                            event.getJobGroup(),
                            event.getJobClass().asSubclass(Job.class),
                            event.getStartAt(),
                            event.getJobParams()
                    );
                }
                return ZonedDateTime.ofInstant(startTime.toInstant(), event.getStartAt().getZone());
            } catch (Exception e) {
                throw new RuntimeException("Failed to create job: " + event.getJobName(), e);
            }
        }).exceptionally(ex -> {
            log.error("Error during job creation: {}", event.getJobName(), ex);
            return null;
        });
    }

    // 2. UPDATE 이벤트 처리 (Cron & Simple)
    @Async
    @EventListener
    public CompletableFuture<ZonedDateTime> handleUpdateEvent(QuartzJobUpdateEvent event) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Date startTime;
                if (event.getNewCronExpression() != null) {
                    // CRON Job 수정
                    startTime = quartzSchedulingService.updateCronJobSchedule(
                            event.getJobName(),
                            event.getJobGroup(),
                            event.getNewStartAt(),
                            event.getNewCronExpression()
                    );
                } else {
                    // Simple Job 수정
                    startTime = quartzSchedulingService.updateSimpleTriggerJob(
                            event.getJobName(),
                            event.getJobGroup(),
                            event.getNewStartAt()
                    );
                }
                return ZonedDateTime.ofInstant(startTime.toInstant(), event.getNewStartAt().getZone());
            } catch (Exception e) {
                throw new RuntimeException("Failed to update job: " + event.getJobName(), e);
            }
        }).exceptionally(ex -> {
            log.error("Error during job update: {}", event.getJobName(), ex);
            return null;
        });
    }

    // 3. DELETE 이벤트 처리
    @Async
    @EventListener
    public CompletableFuture<Boolean> handleDeleteEvent(QuartzJobDeleteEvent event) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                boolean deleted = quartzSchedulingService.deleteJob(event.getJobName(), event.getJobGroup());
                if (deleted) {
                    log.info("Job [{}] in group [{}] deleted successfully", event.getJobName(), event.getJobGroup());
                } else {
                    log.warn("Failed to delete job [{}] in group [{}]", event.getJobName(), event.getJobGroup());
                }
                return deleted;
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete job: " + event.getJobName(), e);
            }
        }).exceptionally(ex -> {
            log.error("Error during job deletion: {}", event.getJobName(), ex);
            return false;
        });
    }

    // 4. READ 이벤트 처리
    @Async
    @EventListener
    public CompletableFuture<JobReadResult> handleReadEvent(QuartzJobReadEvent event) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                JobDetail jobDetail = quartzSchedulingService.getJobDetail(event.getJobName(), event.getJobGroup());
                Trigger trigger = quartzSchedulingService.getTrigger(event.getJobName(), event.getJobGroup());
                return new JobReadResult(jobDetail, trigger);
            } catch (Exception e) {
                throw new RuntimeException("Failed to read job: " + event.getJobName(), e);
            }
        }).exceptionally(ex -> {
            log.error("Error during job read: {}", event.getJobName(), ex);
            return null;
        });
    }

    @Data
    @ToString
    @SuperBuilder
    @AllArgsConstructor
    public static abstract class QuartzJobEvent {
        protected String jobName;
        protected String jobGroup;

        // 공통 메서드 - cronExpression과 startAt을 인자로 받음
        public void checkCronExpression(@Nullable String cronExpression, ZonedDateTime startAt) {
            if (cronExpression != null && !cronExpression.isBlank()) {
                if (!CronExpression.isValidExpression(cronExpression)) {
                    throw new IllegalArgumentException("Invalid cron expression: " + cronExpression);
                }
            }

            // startAt이 1시간 이전 과거인 경우 예외 발생
            ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
            if (startAt.isBefore(now.minusHours(1))) {
                throw new IllegalArgumentException("startAt cannot be earlier than 1 hour in the past");
            }
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder
    @Data
    @ToString
    public static class QuartzJobCreateEvent extends QuartzJobEvent {
        private Class<?> jobClass;
        @Builder.Default
        private ZonedDateTime startAt = ZonedDateTime.now();
        @Nullable
        private String cronExpression;  // CRON Job일 경우 사용, Simple Job은 null 가능
        private Map<String, Object> jobParams;

        public void checkCronExpression() {
            super.checkCronExpression(this.cronExpression, this.startAt);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder
    @ToString
    public static class QuartzJobUpdateEvent extends QuartzJobEvent {
        @Builder.Default
        private ZonedDateTime newStartAt = ZonedDateTime.now();
        private String newCronExpression; // CRON Job일 경우 사용

        public void checkCronExpression() {
            super.checkCronExpression(this.newCronExpression, this.newStartAt);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder
    @ToString
    public static class QuartzJobDeleteEvent extends QuartzJobEvent {
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder
    @ToString
    public static class QuartzJobReadEvent extends QuartzJobEvent {
    }

    @Data
    @Builder
    public static class JobReadResult {
        private JobDetail jobDetail;
        private Trigger trigger;

        public JobReadResult(JobDetail jobDetail, Trigger trigger) {
            this.jobDetail = jobDetail;
            this.trigger = trigger;
        }
    }


}
