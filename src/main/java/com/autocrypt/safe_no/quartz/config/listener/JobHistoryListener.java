package com.autocrypt.safe_no.quartz.config.listener;

import com.autocrypt.logtracer.trace.annotation.LogTrace;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@LogTrace
public class JobHistoryListener implements JobListener {

    private static final Logger logger = LoggerFactory.getLogger(JobHistoryListener.class);

    @Override
    public String getName() {
        return "JobHistoryListener";
    }

    // job이 실행되기 전 기록
    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        logger.info("Job {} is about to be executed. Triggered by: {}", context.getJobDetail().getKey(), context.getTrigger().getKey());
    }

    // job 실행이 취소된 경우
    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        logger.info("Job {} execution was vetoed.", context.getJobDetail().getKey());
    }

    // job이 실행된 후
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        logger.info("Job {} was executed.", context.getJobDetail().getKey());
        if (jobException != null) {
            logger.error("Job {} failed with exception: {}", context.getJobDetail().getKey(), jobException.getMessage());
        }
    }
}