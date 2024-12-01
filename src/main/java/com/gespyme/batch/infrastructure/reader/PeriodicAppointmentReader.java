package com.gespyme.batch.infrastructure.reader;

import com.gespyme.batch.domain.facade.JobFacade;
import com.gespyme.commons.model.job.JobFilterModelApi;
import com.gespyme.commons.model.job.JobModelApi;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PeriodicAppointmentReader implements ItemReader<JobModelApi> {

  private final JobFacade jobFacade;

  @Value("${periodicAppointmentBatchExecutionFrequency}")
  private final String frequency;

  @Override
  public JobModelApi read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    if (Objects.nonNull(frequency)) {
      List<JobModelApi> jobs =
          jobFacade.getPeriodicAppointments(
              JobFilterModelApi.builder()
                  .startDate(LocalDateTime.now().minusDays(Long.parseLong(frequency)))
                  .endDate(LocalDateTime.now())
                  .build());
      if (Objects.nonNull(jobs) && !jobs.isEmpty()) {
        for (JobModelApi job : jobs) {
          return job;
        }
      }
    }
    return null;
  }
}
