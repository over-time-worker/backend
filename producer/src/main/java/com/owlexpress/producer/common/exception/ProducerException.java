package com.owlexpress.producer.common.exception;

public class ProducerException extends RuntimeException {

    public static class ProducerNotFoundException extends RuntimeException{
        public ProducerNotFoundException(String message) {
            super(message);
        }
    }

    public static class ProducerNameDuplicateExceptoin extends RuntimeException{
        public ProducerNameDuplicateExceptoin(String message) {
            super(message);
        }
    }
}
