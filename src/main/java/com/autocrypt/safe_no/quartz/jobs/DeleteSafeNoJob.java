package com.autocrypt.safe_no.quartz.jobs;

import com.autocrypt.logtracer.trace.annotation.LogTrace;
import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import com.autocrypt.safe_no.safe_no.service.SafeNoService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@LogTrace
@RequiredArgsConstructor
public class DeleteSafeNoJob implements Job {

    private final SafeNoService safeNoService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String driveId = jobDataMap.getString("driveId");
        SafeNoProperties.ServiceEnum serviceId = (SafeNoProperties.ServiceEnum)jobDataMap.get(SafeNoProperties.ServiceEnum.class.getSimpleName());

        safeNoService.deleteFinishedDrive(driveId, serviceId);
    }
}
