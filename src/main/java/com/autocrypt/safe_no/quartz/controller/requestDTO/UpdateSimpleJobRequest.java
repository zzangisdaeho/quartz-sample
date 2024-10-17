package com.autocrypt.safe_no.quartz.controller.requestDTO;

import java.time.ZonedDateTime;

public record UpdateSimpleJobRequest(
       String jobName,
       String jobGroup,
       ZonedDateTime newStartAt
) {
}