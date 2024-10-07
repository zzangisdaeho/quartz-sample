package com.autocrypt.safeno.quartz.controller.requestDTO;

public record DeleteJobRequest(
       String jobName,
       String jobGroup
) {
}