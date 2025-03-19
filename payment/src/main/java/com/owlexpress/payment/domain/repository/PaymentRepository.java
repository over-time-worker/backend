package com.owlexpress.payment.domain.repository;

import com.owlexpress.payment.domain.entity.Payment;
import java.util.Optional;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findByTransactionId(String transactionId);
}
