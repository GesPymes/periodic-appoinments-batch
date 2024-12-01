package com.gespyme.batch.infrastructure.writer;

import com.gespyme.batch.domain.facade.JobFacade;
import com.gespyme.commons.model.job.AppointmentModelApi;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PeriodicAppointmentWriter implements ItemWriter<AppointmentModelApi> {

  private final JobFacade jobFacade;

  @Override
  public void write(Chunk<? extends AppointmentModelApi> chunk) {
    List<AppointmentModelApi> items = (List<AppointmentModelApi>) chunk.getItems();
    items.forEach(jobFacade::createAppointment);
  }
}
