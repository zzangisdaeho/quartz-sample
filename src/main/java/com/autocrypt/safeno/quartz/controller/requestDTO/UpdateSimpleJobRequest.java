package com.autocrypt.safeno.quartz.controller.requestDTO;

import java.util.Date;

public record UpdateSimpleJobRequest(
       String jobName,
       String jobGroup,
       Date newStartAt
) {
}