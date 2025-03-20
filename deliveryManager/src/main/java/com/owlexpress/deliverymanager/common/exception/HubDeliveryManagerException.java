package com.owlexpress.deliverymanager.common.exception;

public class HubDeliveryManagerException extends RuntimeException {

    public static class NotAuthorizedException extends RuntimeException{
        public NotAuthorizedException(String message) {
            super(message);
        }
    }

    public static class HubDeliveryManagerNotFoundException extends RuntimeException{
        public HubDeliveryManagerNotFoundException(String message) {
            super(message);
        }
    }

    public static class HubDeliveryManagerNameDuplicateException extends RuntimeException{
        public HubDeliveryManagerNameDuplicateException(String message) {
            super(message);
        }
    }
}
