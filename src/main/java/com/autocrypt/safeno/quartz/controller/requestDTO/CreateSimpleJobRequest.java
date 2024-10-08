package com.autocrypt.safeno.quartz.controller.requestDTO;

import java.time.ZonedDateTime;
import java.util.Map;

public record CreateSimpleJobRequest(
       String jobName,
       String jobGroup,
       String jobClassName,
       ZonedDateTime startAt,
       Map<String, Object>jobParams
) {
}
