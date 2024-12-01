package com.gespyme.batch.infrastructure.rest;

import com.gespyme.batch.domain.facade.JobFacade;
import com.gespyme.commons.model.job.AppointmentFilterModelApi;
import com.gespyme.commons.model.job.AppointmentModelApi;
import com.gespyme.commons.model.job.JobFilterModelApi;
import com.gespyme.commons.model.job.JobModelApi;
import com.gespyme.rest.RestCallService;
import com.gespyme.rest.RestRequest;
import com.google.gson.Gson;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobApiRestCallService implements JobFacade {

  private final Gson gson;

  @Value("${job.endpoint}")
  private String jobEndpoint;

  private final RestCallService<JobModelApi, JobModelApi> restService;
  private final RestCallService<AppointmentModelApi, AppointmentModelApi> appoinmentRestService;
  ParameterizedTypeReference<List<JobModelApi>> typeReference =
      new ParameterizedTypeReference<List<JobModelApi>>() {};
  ParameterizedTypeReference<List<AppointmentModelApi>> appointmentTypeReference =
      new ParameterizedTypeReference<List<AppointmentModelApi>>() {};

  @Override
  public List<JobModelApi> getPeriodicAppointments(JobFilterModelApi jobFilterModelApi) {
    return restService.performGetCallForList(
        RestRequest.builder()
            .server(jobEndpoint)
            .path("/job/findPeendingJobs")
            .headers(Map.of("Content-Type", "application/json"))
            .queryParams(
                jobFilterModelApi.selectedParamsMap().entrySet().stream()
                    .collect(
                        Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue() == null ? "" : entry.getValue().toString())))
            .build(),
        typeReference);
  }

  @Override
  public List<AppointmentModelApi> getAppointments(
      AppointmentFilterModelApi appointmentFilterModelApi) {
    return appoinmentRestService.performGetCallForList(
        RestRequest.builder()
            .server(jobEndpoint)
            .path("/appointment/")
            .headers(Map.of("Content-Type", "application/json"))
            .queryParams(
                appointmentFilterModelApi.selectedParamsMap().entrySet().stream()
                    .collect(
                        Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue() == null ? "" : entry.getValue().toString())))
            .build(),
        appointmentTypeReference);
  }

  @Override
  public Optional<AppointmentModelApi> createAppointment(AppointmentModelApi appointment) {
    return appoinmentRestService.performPostCall(
        RestRequest.builder()
            .server(jobEndpoint)
            .path("/appointment")
            .headers(Map.of("Content-Type", "application/json"))
            .body(gson.toJson(appointment))
            .build(),
        AppointmentModelApi.class,
        AppointmentModelApi.class);
  }
}
