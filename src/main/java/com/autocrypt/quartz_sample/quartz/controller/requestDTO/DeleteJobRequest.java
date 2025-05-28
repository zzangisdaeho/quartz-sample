package com.autocrypt.quartz_sample.quartz.controller.requestDTO;

public record DeleteJobRequest(
       String jobName,
       String jobGroup
) {
}