package com.owlexpress.producer.domain.repository;

import com.owlexpress.producer.domain.entity.Producer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Optional;

public interface ProducerRepository {
    Producer save(Producer producer);

    Optional<Producer> findByCompanyName(@Size(min = 1, max = 20) @NotNull(message = "회사명은 필수값입니다.") String companyName);
}
