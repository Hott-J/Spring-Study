package com.community.batch.jobs.inactive.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

/**
 * Created by young891221@gmail.com on 2018-04-15
 * Blog : http://haviyj.tistory.com
 * Github : http://github.com/young891221
 */
@Slf4j // 필드에 로그 객체를 따로 생성할 필요 없이 로그 객체를 사용할 수 있도록 설정하는 롬복 어노테이션
@Component // 외부에서 InactiveJobListener를 주입받아서 사용할 수 있게 스프링 빈으로 등록
public class InactiveIJobListener implements JobExecutionListener { // Job 실행 전후에 특정 로직을 담을 수 있도록 제공되는 인터페이스

    /**
     * Job 실행 전에 수행될 로직을 구현하는 메서드
     * @param jobExecution
     */
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Before Job");
    }

    /**
     * Job 실행 후에 수행될 로직을 구현하는 메서드
     * @param jobExecution
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("After Job");
    }
}