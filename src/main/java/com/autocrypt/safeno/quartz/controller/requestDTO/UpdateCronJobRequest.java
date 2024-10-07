package com.autocrypt.safeno.quartz.controller.requestDTO;

import java.util.Date;

public record UpdateCronJobRequest(
       String jobName,
       String jobGroup,
       Date newStartAt,
       String newCronExpression
) {
}