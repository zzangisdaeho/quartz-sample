package com.autocrypt.safe_no.quartz.controller.requestDTO;

public record DeleteJobRequest(
       String jobName,
       String jobGroup
) {
}