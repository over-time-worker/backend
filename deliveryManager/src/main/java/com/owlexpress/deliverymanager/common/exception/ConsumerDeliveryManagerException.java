package com.owlexpress.deliverymanager.common.exception;

public class ConsumerDeliveryManagerException extends RuntimeException {

    public static class ConsumerDeliveryManagerNotFoundException extends RuntimeException{
        public ConsumerDeliveryManagerNotFoundException(String message) {
            super(message);
        }
    }

    public static class ConsumerDeliveryManagerNameDuplicateException extends RuntimeException{
        public ConsumerDeliveryManagerNameDuplicateException(String message) {
            super(message);
        }
    }
}
