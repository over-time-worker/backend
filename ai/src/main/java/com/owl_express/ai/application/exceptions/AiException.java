package com.owl_express.ai.application.exceptions;

public class AiException extends RuntimeException {

    public static class MessageNotFoundException extends RuntimeException{
        public MessageNotFoundException(String message) {
            super(message);
        }
    }

}
