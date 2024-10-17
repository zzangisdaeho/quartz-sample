package com.autocrypt.safe_no.quartz.config.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "quartz.enabled", havingValue = "true")
@Slf4j
public class QuartzListenerConfig {

    private final Scheduler scheduler;

    public QuartzListenerConfig(Scheduler scheduler) {
        this.scheduler = scheduler;
        addJobListener();
    }

    public void addJobListener() {
        try {
            scheduler.getListenerManager().addJobListener(new JobHistoryListener());
            log.info("JobHistoryListener added");
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to add Job Listener", e);
        }
    }
}