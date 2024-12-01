package com.gespyme.batch.infrastructure.processor;

import com.gespyme.batch.infrastructure.rest.JobApiRestCallService;
import com.gespyme.commons.model.job.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PeriodicAppointmentProcessor
    implements ItemProcessor<JobModelApi, AppointmentModelApi> {

  private JobApiRestCallService jobApiRestCallService;

  @Override
  public AppointmentModelApi process(JobModelApi item) {

    List<AppointmentModelApi> appointmentModelApiList =
        jobApiRestCallService.getAppointments(
            AppointmentFilterModelApi.builder().jobId(item.getJobId()).build());

    AppointmentModelApi lastAppointment =
        appointmentModelApiList.stream().sorted(x -> x.getStartDate()).findFirst();

    LocalDateTime start =
        calculateNextAppointmentStartDate(lastAppointment.getStartDate(), item.getPeriodicity());
    LocalDateTime end =
        calculateNextAppointmentEndDate(
            start, calculateJobDuration(start, lastAppointment.getEndDate()));
    return getAppointmentModelApi(start, end);
  }

  private AppointmentModelApi getAppointmentModelApi(LocalDateTime start, LocalDateTime end) {
    return AppointmentModelApi.builder()
        .status(AppointmentStatus.PENDING)
        .startDate(start)
        .endDate(end)
        .build();
  }

  private LocalDateTime calculateNextAppointmentEndDate(LocalDateTime startDate, int jobDuration) {
    return startDate.plusDays(jobDuration);
  }

  private LocalDateTime calculateNextAppointmentStartDate(
      LocalDateTime lastEndDate, int periodicity) {
    return lastEndDate.plusDays(periodicity);
  }

  private int calculateJobDuration(LocalDateTime start, LocalDateTime end) {
    return (int) ChronoUnit.DAYS.between(start, end);
  }
}
