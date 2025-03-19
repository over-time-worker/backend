package com.owlexpress.producer.common.exception;

public class ProducerException extends RuntimeException {

    public static class NotAuthorizedException extends RuntimeException{
        public NotAuthorizedException(String message) {
            super(message);
        }
    }

    public static class ProducerNotFoundException extends RuntimeException{
        public ProducerNotFoundException(String message) {
            super(message);
        }
    }

    public static class ProducerNameDuplicateException extends RuntimeException{
        public ProducerNameDuplicateException(String message) {
            super(message);
        }
    }
}
