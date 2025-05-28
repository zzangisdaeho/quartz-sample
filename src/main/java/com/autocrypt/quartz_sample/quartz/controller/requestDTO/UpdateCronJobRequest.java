package com.autocrypt.quartz_sample.quartz.controller.requestDTO;

import java.time.ZonedDateTime;

public record UpdateCronJobRequest(
       String jobName,
       String jobGroup,
       ZonedDateTime newStartAt,
       String newCronExpression
) {
}