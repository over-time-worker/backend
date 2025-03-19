package com.owlexpress.payment.infrastructure.repository;

import com.owlexpress.payment.domain.entity.Payment;
import com.owlexpress.payment.domain.repository.PaymentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentQueryRepository paymentQueryRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }

    @Override
    public Optional<Payment> findByTransactionId(String transactionId) {
        return paymentJpaRepository.findPaymentByTransactionId(transactionId);
    }
}
