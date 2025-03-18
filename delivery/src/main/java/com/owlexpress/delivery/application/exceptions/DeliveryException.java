package com.owlexpress.delivery.application.exceptions;

public class DeliveryException extends RuntimeException {

    public static class AlarmFeignClientException extends RuntimeException {
        public AlarmFeignClientException(String message) {
            super(message);
        }
    }

    public static class AlarmNotFoundException extends RuntimeException {
        public AlarmNotFoundException(String message) {
            super(message);
        }
    }

}
