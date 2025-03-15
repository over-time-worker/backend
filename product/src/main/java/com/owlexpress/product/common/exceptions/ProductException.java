package com.owlexpress.product.common.exceptions;

public class ProductException extends RuntimeException {

    public static class ProductNotFoundException extends RuntimeException{
        public ProductNotFoundException(String message) {
            super(message);
        }
    }

    public static class ProductNameDuplicateExceptoin extends RuntimeException{
        public ProductNameDuplicateExceptoin(String message) {
            super(message);
        }
    }

    public static class HubInfoNotFoundException extends RuntimeException {
        public HubInfoNotFoundException(String message) { super(message); }
    }
}
