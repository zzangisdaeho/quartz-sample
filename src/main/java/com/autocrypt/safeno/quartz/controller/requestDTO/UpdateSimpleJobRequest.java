package com.autocrypt.safeno.quartz.controller.requestDTO;

import java.time.ZonedDateTime;
import java.util.Date;

public record UpdateSimpleJobRequest(
       String jobName,
       String jobGroup,
       ZonedDateTime newStartAt
) {
}