package com.owlexpress.payment.domain.repository;

import com.owlexpress.payment.domain.entity.Payment;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findByOrderId(UUID orderId);
}
