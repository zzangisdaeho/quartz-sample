package com.autocrypt.quartz_sample.quartz.config.listener;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class QuartzListenerConfig {

    private final Scheduler scheduler;

    public QuartzListenerConfig(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @PostConstruct
    public void addJobListener() {
        try {
            scheduler.getListenerManager().addJobListener(new JobHistoryListener());
            log.info("JobHistoryListener added");
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to add Job Listener", e);
        }
    }
}