package com.autocrypt.quartz_sample.quartz.controller.requestDTO;

import java.time.ZonedDateTime;

public record UpdateSimpleJobRequest(
       String jobName,
       String jobGroup,
       ZonedDateTime newStartAt
) {
}