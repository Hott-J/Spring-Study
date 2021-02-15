package com.example.SpringBootCommunityBatch.jobs.inactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import java.util.Random;

@Slf4j
public class InactiveJobExecutionDecider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        if(new Random().nextInt() > 0) {
            log.info("FlowExecutionStatus.COMPLETED");
            return FlowExecutionStatus.COMPLETED;
        }
        log.info("FlowExecutionStatus.FAILED");
        return FlowExecutionStatus.FAILED;
    }
}
