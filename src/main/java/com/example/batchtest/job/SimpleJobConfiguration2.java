package com.example.batchtest.job;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration
public class SimpleJobConfiguration2 {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job simpleJob1() {
        return jobBuilderFactory.get("simpleJob1")
                .start(simpleStep3())
                .next(simpleStep4(null))
                .build();
    }

    private final SimpleTasklet tasklet1;

    @Bean
    @JobScope
    public Step simpleStep3() {
        return stepBuilderFactory.get("simpleStep3")
                .tasklet(tasklet1)
                .build();
    }

    @Bean
    @JobScope
    public Step simpleStep4(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("simpleStep4")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step4");
                    log.info(">>>>> requestDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}