package com.owlexpress.payment.common.exception;

public class PaymentException {

    private static final String PAYMENT_DOES_NOT_MATCH = "일치하는 결제 내역이 없습니다.";
    private static final String ORDER_DOES_NOT_EXIST = "찾으시는 주문 내역이 없습니다.";
    private static final String PAYMENT_DOES_NOT_LINKED_WITH_ORDER = "해당 주문 내역에 대한 결제 내역이 없습니다.";
    private static final String PAYMENT_CANCEL_FAILED = "결제 취소 중 문제가 발생했습니다.";

    public static class PaymentNotFoundException extends RuntimeException {

        public PaymentNotFoundException() {
            super(PAYMENT_DOES_NOT_MATCH);
        }
    }

    public static class PaymentCancelFailedException extends RuntimeException {

        public PaymentCancelFailedException() {
            super(PAYMENT_CANCEL_FAILED);
        }
    }

    public static class OrderNotFoundException extends RuntimeException {

        public OrderNotFoundException() {
            super(ORDER_DOES_NOT_EXIST);
        }
    }

    public static class OrderDoesNotMatchException extends RuntimeException{

        public OrderDoesNotMatchException() {
            super(PAYMENT_DOES_NOT_LINKED_WITH_ORDER);
        }
    }

}
