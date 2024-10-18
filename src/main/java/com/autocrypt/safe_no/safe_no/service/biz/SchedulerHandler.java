package com.autocrypt.safe_no.safe_no.service.biz;

import com.autocrypt.safe_no.quartz.jobs.DeleteSafeNoJob;
import com.autocrypt.safe_no.quartz.service.QuartzSchedulingEventListener;
import com.autocrypt.safe_no.quartz.service.QuartzSchedulingService;
import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import com.autocrypt.safe_no.safe_no.util.SafeNoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulerHandler {

    @Value("${quartz.service.safe-no.job-name-prefix}")
    private String jobPrefix;

    @Value("${quartz.service.safe-no.job-group-prefix}")
    private String jobGroupPrefix;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final Optional<QuartzSchedulingService> quartzSchedulingService;

    public void registerScheduler(String jobId, SafeNoProperties.ServiceEnum serviceId) {
        if (quartzSchedulingService.isPresent()) {
            try {
                JobDetail existingJob = quartzSchedulingService.get().getJobDetail(jobPrefix + jobId, jobGroupPrefix);

                if (existingJob == null) {
                    applicationEventPublisher.publishEvent(
                            QuartzSchedulingEventListener.QuartzJobCreateEvent.builder()
                                    .startAt(ZonedDateTime.now().plus(SafeNoUtil.getServiceProperty(serviceId).getDeleteTime()))
                                    .jobClass(DeleteSafeNoJob.class)
                                    .jobName(jobPrefix + jobId)
                                    .jobGroup(jobGroupPrefix)
                                    .jobParams(Map.of(
                                            SafeNoProperties.ServiceEnum.class.getSimpleName(),
                                            serviceId.name(), "driveId", jobId))
                                    .build());
                } else {
                    log.info("Job [{}] in group [{}] already exists. Skipping registration.", jobPrefix + jobId, jobGroupPrefix);
                }
            } catch (SchedulerException e) {
                log.error("Error checking for existing job: [{}] in group [{}]", jobPrefix + jobId, jobGroupPrefix, e);
            }
        } else {
            log.warn("QuartzSchedulingService 빈이 존재하지 않습니다. 스케줄러 작업을 생략합니다.");
        }
    }

    public void delaySchedule(String jobId) {
        if (quartzSchedulingService.isPresent()) {
            try {
                JobDetail existingJob = quartzSchedulingService.get().getJobDetail(jobPrefix + jobId, jobGroupPrefix);

                if (existingJob != null) {
                    SafeNoProperties.ServiceEnum serviceId = SafeNoProperties.ServiceEnum.from(
                            existingJob.getJobDataMap().getString(SafeNoProperties.ServiceEnum.class.getSimpleName())
                    );

                    applicationEventPublisher.publishEvent(
                            QuartzSchedulingEventListener.QuartzJobUpdateEvent.builder()
                                    .newStartAt(ZonedDateTime.now().plus(SafeNoUtil.getServiceProperty(serviceId).getDeleteTime()))
                                    .jobName(jobPrefix + jobId)
                                    .jobGroup(jobGroupPrefix)
                                    .build()
                    );
                } else {
                    log.warn("Job [{}] in group [{}] not exist. Skipping delay.", jobPrefix + jobId, jobGroupPrefix);
                }
            } catch (SchedulerException e) {
                log.error("Error checking for existing job: [{}] in group [{}]", jobPrefix + jobId, jobGroupPrefix, e);
            }
        } else {
            log.warn("QuartzSchedulingService 빈이 존재하지 않습니다. 스케줄러 작업을 생략합니다.");
        }
    }
}
