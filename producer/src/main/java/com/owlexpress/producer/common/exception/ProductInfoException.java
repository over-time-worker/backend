package com.owlexpress.producer.common.exception;

public class ProductInfoException extends RuntimeException {

    public static class ProductInfoNotFoundException extends RuntimeException{
        public ProductInfoNotFoundException(String message) {
            super(message);
        }
    }

    public static class ProductInfoNameDuplicateExceptoin extends RuntimeException{
        public ProductInfoNameDuplicateExceptoin(String message) {
            super(message);
        }
    }
}
