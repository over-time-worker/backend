package com.owlexpress.delivery.application.exceptions;

public class DeliveryException extends RuntimeException {

    public static class AlarmFeignClientException extends RuntimeException {
        public AlarmFeignClientException(String message) {
            super(message);
        }
    }

    public static class DeliveryNotFoundException extends RuntimeException {
        public DeliveryNotFoundException(String message) {
            super(message);
        }
    }

    public static class DeliveryHistoryNotFoundException extends RuntimeException {
        public DeliveryHistoryNotFoundException(String message) {
            super(message);
        }
    }

    public static class NotSupportedPlatformTypeException extends RuntimeException {
        public NotSupportedPlatformTypeException(String message) {
            super(message);
        }
    }

    public static class NotSupportedDeliveryStatusException extends RuntimeException {
        public NotSupportedDeliveryStatusException(String message) {
            super(message);
        }
    }

    public static class DeliveryDeleteFailException extends RuntimeException {
        public DeliveryDeleteFailException(String message) {
            super(message);
        }
    }

}
