package com.owlexpress.producer.domain.repository;

import com.owlexpress.producer.domain.entity.Producer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.UUID;

public interface ProducerRepository {
    Producer save(Producer producer);

    Optional<Producer> findByCompanyName(@Size(min = 1, max = 20) @NotNull(message = "회사명은 필수값입니다.") String companyName);

    Optional<Producer> findById(UUID producerId);

    Page<Producer> searchProducer(
            String sort,
            String q,
            String orderBy,
            PageRequest pageRequest
    );
}
