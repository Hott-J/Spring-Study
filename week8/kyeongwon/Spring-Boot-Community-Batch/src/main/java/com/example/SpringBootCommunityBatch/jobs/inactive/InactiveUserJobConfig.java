package com.example.SpringBootCommunityBatch.jobs.inactive;

import com.example.SpringBootCommunityBatch.domain.User;
import com.example.SpringBootCommunityBatch.domain.enums.UserStatus;
import com.example.SpringBootCommunityBatch.jobs.inactive.listener.InactiveJobListener;
import com.example.SpringBootCommunityBatch.jobs.inactive.listener.InactiveStepListener;
import com.example.SpringBootCommunityBatch.jobs.readers.QueueItemReader;
import com.example.SpringBootCommunityBatch.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@AllArgsConstructor
@Configuration
public class InactiveUserJobConfig {

    private final static int CHUNK_SIZE = 15;

    private final EntityManagerFactory entityManagerFactory;

    private UserRepository userRepository;

    @Bean
    public Job inactiveUserJob(JobBuilderFactory jobBuilderFactory, InactiveJobListener inactiveJobListener,
                               Flow multiFlow) {
        return jobBuilderFactory.get("inactiveUserJob")
                .preventRestart()
                .listener(inactiveJobListener)
                .start(multiFlow)
                .end()
                .build();
    }

    @Bean
    public Flow multiFlow(Step inactiveJobStep) {
        Flow flows[] = new Flow[5];
        IntStream.range(0, flows.length).forEach(i -> flows[i] = new FlowBuilder<Flow>("MultiFlow"+i)
                .from(inactiveJobFlow(inactiveJobStep)).end());

        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("MultiFlowTest");
        return flowBuilder
                .split(taskExecutor())
                .add(flows)
                .build();
    }

    //빈으로 생성하면 싱글톤이기 때문에 멀티 Flow를 위해서는 빈으로 등록하면 안됨
    @Bean
    public Flow inactiveJobFlow(Step inactiveJobStep) {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("inactiveJobFlow");
        return flowBuilder
                .start(new InactiveJobExecutionDecider())
                .on(FlowExecutionStatus.FAILED.getName()).end()
                .on(FlowExecutionStatus.COMPLETED.getName()).to(inactiveJobStep)
                .end();
    }

    @Bean
    public Step inactiveJobStep(StepBuilderFactory stepBuilderFactory, ListItemReader<User> inactiveUserReader,
                                InactiveStepListener inactiveStepListener, TaskExecutor taskExecutor) {
        return stepBuilderFactory.get("inactiveUserStep")
                .<User, User> chunk(CHUNK_SIZE)
                .reader(inactiveUserReader)
                .processor(inactiveUserProcessor())
                .writer(inactiveUserWriter())
                .listener(inactiveStepListener)
                .taskExecutor(taskExecutor)
                .throttleLimit(2)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("Batch_Task");
    }

    @Bean
    @StepScope
    public ListItemReader<User> inactiveUserReader(@Value("#{jobParameters[nowDate]}") Date nowDate,
                                                   UserRepository userRepository) {
        LocalDateTime now = LocalDateTime.ofInstant(nowDate.toInstant(), ZoneId.systemDefault());
        List<User> inactiveUsers = userRepository.findByUpdatedDateBeforeAndStatusEquals(now.minusYears(1),
                UserStatus.ACTIVE);
        return new ListItemReader<>(inactiveUsers);
    }

    // 휴먼 회원으로 전환
    public ItemProcessor<User, User> inactiveUserProcessor() {
        return User::setInactive;
//        return new ItemProcessor<User, User>() {
//            @Override
//            public User process(User user) throws Exception {
//                return user.setInactive();
//            }
//        };
    }

    // 휴먼회원을 DB에 저장
//    public ItemWriter<User> inactiveUserWriter() {
//        return ((List<? extends User> users) -> userRepository.saveAll(users));
//    }

    private JpaItemWriter<User> inactiveUserWriter() {
        JpaItemWriter<User> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

}
