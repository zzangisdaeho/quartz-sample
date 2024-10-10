package com.autocrypt.safeno.quartz.controller.requestDTO;

import java.time.ZonedDateTime;
import java.util.Date;

public record UpdateCronJobRequest(
       String jobName,
       String jobGroup,
       ZonedDateTime newStartAt,
       String newCronExpression
) {
}