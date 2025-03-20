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

    public static class ConsumerDuplicateAssignNumberException extends Throwable {
        public ConsumerDuplicateAssignNumberException(String meesage) {
        }
    }

    public static class ConsumerDeliveryManagerNotAvailableException extends Throwable {
        public ConsumerDeliveryManagerNotAvailableException(String message) {
        }
    }



    public static class HubNotFoundException extends Throwable {
        public HubNotFoundException(String message) {
        }
    }

    public static class ConsumerEmptyException extends Throwable {
        public ConsumerEmptyException(String message) {
        }
    }
}
