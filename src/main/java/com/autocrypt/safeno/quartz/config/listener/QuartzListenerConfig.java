package com.autocrypt.safeno.quartz.config.listener;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzListenerConfig {

    private final Scheduler scheduler;

    public QuartzListenerConfig(Scheduler scheduler) {
        this.scheduler = scheduler;
        addJobListener();
    }

    public void addJobListener() {
        try {
            scheduler.getListenerManager().addJobListener(new JobHistoryListener());
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to add Job Listener", e);
        }
    }
}