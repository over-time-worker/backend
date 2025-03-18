package com.owlexpress.consumer.common.exceptions;

public class ConsumerException extends RuntimeException {

    public static class ConsumerNotFoundException extends RuntimeException{
        public ConsumerNotFoundException(String message) {
            super(message);
        }
    }

    public static class ConsumerNameDuplicateExceptoin extends RuntimeException{
        public ConsumerNameDuplicateExceptoin(String message) {
            super(message);
        }
    }

    public static class FeignClientException extends RuntimeException{
        public FeignClientException(String message) {
            super(message);
        }
    }
}
