package com.autocrypt.safe_no.quartz.controller.requestDTO;


import java.time.ZonedDateTime;
import java.util.Map;

public record CreateCronJobRequest(
        String jobName,
        String jobGroup,
        String jobClassName,
        ZonedDateTime startAt,
        Map<String, Object>jobParams,
        String cronExpression
) {
}