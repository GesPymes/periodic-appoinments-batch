package com.gespyme.batch.infrastructure.config;

import com.gespyme.batch.infrastructure.processor.PeriodicAppointmentProcessor;
import com.gespyme.batch.infrastructure.reader.PeriodicAppointmentReader;
import com.gespyme.batch.infrastructure.writer.PeriodicAppointmentWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class PeriodicAppointmentBatchConfig {

    private final PeriodicAppointmentReader reader;
    private final PeriodicAppointmentProcessor processor;
    private final PeriodicAppointmentWriter writer;

    @Bean
    public Job periodicAppointmentJob(JobBuilderFactory jobBuilderFactory, Step deleteLimitStep) {
        return jobBuilderFactory
                .get("deleteLimitsJob")
                .listener(jobCompletionNotificationListener)
                .incrementer(new RunIdIncrementer())
                .flow(deleteLimitStep)
                .end()
                .build();
    }

    @Bean
    public Step deleteLimitStep(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory
                .get("deleteLimitStep")
                .<List<CustomerLimit>, List<CustomerLimit>>chunk(1)
                .reader(payLimitsReader)
                .writer(payLimitsWriter)
                .build();
    }
}

}
