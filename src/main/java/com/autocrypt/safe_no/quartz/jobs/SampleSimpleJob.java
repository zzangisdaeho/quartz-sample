package com.autocrypt.safe_no.quartz.jobs;

import com.autocrypt.logtracer.trace.annotation.LogTrace;
import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.env.Environment;

@Slf4j
@LogTrace
@RequiredArgsConstructor
public class SampleSimpleJob implements Job {

    private final SafeNoProperties safenoProperties;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        // JobDataMap에서 매개변수를 가져옴
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        // JobDataMap을 사람이 읽을 수 있는 형태로 출력
        jobDataMap.forEach((key, value) -> log.info("JobDataMap Entry - Key: {}, Value: {}", key, value));

        // 실제로 스케줄링할 작업 로직을 여기서 처리
        log.info("Sample Job is being executed...");
    }
}