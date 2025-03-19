package com.owlexpress.payment.domain.repository;

import com.owlexpress.payment.domain.entity.Payment;

public interface PaymentRepository {

    Payment save(Payment payment);
}
