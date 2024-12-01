package com.gespyme.batch.domain.facade;

import com.gespyme.commons.model.job.AppointmentFilterModelApi;
import com.gespyme.commons.model.job.AppointmentModelApi;
import com.gespyme.commons.model.job.JobFilterModelApi;
import com.gespyme.commons.model.job.JobModelApi;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JobFacade {
  List<JobModelApi> getPeriodicAppointments(JobFilterModelApi jobFilterModelApi);

  Optional<AppointmentModelApi> createAppointment(AppointmentModelApi appointment);

  List<AppointmentModelApi> getAppointments(AppointmentFilterModelApi appointmentFilterModelApi);
}
