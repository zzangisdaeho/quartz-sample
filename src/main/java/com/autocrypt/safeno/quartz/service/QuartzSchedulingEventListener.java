package com.autocrypt.safeno.quartz.service;

import com.autocrypt.logtracer.trace.annotation.LogTrace;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

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

    @Async
    @EventListener
    public CompletableFuture<ZonedDateTime> handleCreateEvent(QuartzJobCreateEvent event) {
        try {
            Date startTime = null;
            if (event.getCronExpression() != null) {
                startTime =
                        quartzSchedulingService.scheduleCronJob(
                                event.getJobName(),
                                event.getJobGroup(),
                                event.getJobClass().asSubclass(Job.class),
                                event.getStartAt(),
                                event.getCronExpression(),
                                event.getJobParams()
                        );
            } else {
                startTime =
                        quartzSchedulingService.scheduleOneTimeJob(
                                event.getJobName(),
                                event.getJobGroup(),
                                event.getJobClass().asSubclass(Job.class),
                                event.getStartAt(),
                                event.getJobParams()
                        );
            }
            return CompletableFuture.completedFuture(ZonedDateTime.ofInstant(startTime.toInstant(), event.getStartAt().getZone()));
        } catch (Exception e) {
            throw new RuntimeException("Error creating job: " + event.getJobName(), e);
        }
    }


    @Async
    @EventListener
    public CompletableFuture<ZonedDateTime> handleUpdateEvent(QuartzJobUpdateEvent event) {

        try {
            Date startTime = null;

            if (event.getNewCronExpression() != null) {
                startTime = quartzSchedulingService.updateCronJobSchedule(
                        event.getJobName(),
                        event.getJobGroup(),
                        event.getNewStartAt(),
                        event.getNewCronExpression()
                );
            } else {
                startTime = quartzSchedulingService.updateSimpleTriggerJob(
                        event.getJobName(),
                        event.getJobGroup(),
                        event.getNewStartAt()
                );
            }
            return CompletableFuture.completedFuture(ZonedDateTime.ofInstant(startTime.toInstant(), event.getNewStartAt().getZone()));

        } catch (Exception e) {
            throw new RuntimeException("Error updating job: " + event.getJobName(), e);
        }
    }

    @Async
    @EventListener
    public void handleDeleteEvent(QuartzJobDeleteEvent event) {
        try{
            quartzSchedulingService.deleteJob(event.getJobName(), event.getJobGroup());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting job: " + event.getJobName(), e);
        }
    }

    @Async
    @EventListener
    public CompletableFuture<Pair<JobDetail, Trigger>> handleReadEvent(QuartzJobReadEvent event) {
        try{
            return CompletableFuture.completedFuture(Pair.of(
                quartzSchedulingService.getJobDetail(event.getJobName(), event.getJobGroup()),
                quartzSchedulingService.getTrigger(event.getJobName(), event.getJobGroup())
            ));
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }



    @Data
    @Builder
    @ToString
    public static class QuartzJobCreateEvent {
        private String jobName;
        private String jobGroup;
        private Class<?> jobClass;
        @Builder.Default
        private ZonedDateTime startAt = ZonedDateTime.now();
        private String cronExpression;  // CRON Job일 경우 사용, Simple Job은 null 가능
        private Map<String, Object> jobParams;

        public boolean checkCronExpression() {
            // CRON expression이 있는 경우 유효한 표현식인지 검증
            if (this.cronExpression != null && !this.cronExpression.isBlank()) {
                if (!CronExpression.isValidExpression(this.cronExpression))
                    throw new IllegalArgumentException("Invalid cron expression: " + this.cronExpression);
            }

            // startAt이 1시간 이전 과거인 경우 예외 발생
            ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
            if (this.startAt.isBefore(now.minusHours(1))) {
                throw new IllegalArgumentException("startAt cannot be earlier than 1 hour in the past");
            }
        }
    }

    @Data
    @Builder
    @ToString
    public class QuartzJobUpdateEvent {
        private String jobName;
        private String jobGroup;
        @Builder.Default
        private ZonedDateTime newStartAt = ZonedDateTime.now();
        private String newCronExpression; // CRON Job일 경우 사용

    }

    @Data
    @Builder
    @ToString
    public class QuartzJobDeleteEvent {
        private String jobName;
        private String jobGroup;
    }

    @Data
    @Builder
    @ToString
    public class QuartzJobReadEvent {
        private String jobName;
        private String jobGroup;
    }


}
