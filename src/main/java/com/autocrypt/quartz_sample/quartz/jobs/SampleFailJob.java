package com.autocrypt.quartz_sample.quartz.jobs;

import com.autocrypt.logtracer.trace.annotation.LogTrace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
@LogTrace
@RequiredArgsConstructor
public class SampleFailJob extends QuartzJobBean {

    private Environment environment;
    private ApplicationArguments applicationArguments;

    private int retryCount;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

//        try{
            JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

            // JobDataMap을 사람이 읽을 수 있는 형태로 출력
            jobDataMap.forEach((key, value) -> log.info("JobDataMap Entry - Key: {}, Value: {}", key, value));


            // 실제로 스케줄링할 작업 로직을 여기서 처리
            log.info("Fail Job is being executed...");

            jobDataMap.put("fail", "yes");

            throw new RuntimeException("something wrong");

//        }catch (Exception e){
////            if (retryCount >= 3) {
////                log.warn("Retry limit reached, not retrying.");
////                return;
////            }
////            retryCount++;
////
////        JobExecutionException ex = new JobExecutionException(e, true);
//        JobExecutionException ex = new JobExecutionException(e);
//        ex.setUnscheduleAllTriggers(true);
//        throw ex;
////
//        }


    }
}
