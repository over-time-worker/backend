package com.owlexpress.payment.infrastructure.repository;

import com.owlexpress.payment.domain.entity.Payment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findPaymentByTransactionId(String transactionId);
}
