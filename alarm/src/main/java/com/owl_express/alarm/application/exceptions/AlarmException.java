package com.owl_express.alarm.application.exceptions;

public class AlarmException extends RuntimeException {

    public static class AiFeignClientException extends RuntimeException {
        public AiFeignClientException(String message) {
            super(message);
        }
    }

    public static class AlarmNotFoundException extends RuntimeException {
        public AlarmNotFoundException(String message) {
            super(message);
        }
    }

    public static class NotSupportedPlatformTypeException extends RuntimeException {
        public NotSupportedPlatformTypeException(String message) {
            super(message);
        }
    }

    public static class NotSupportedMessageTypeException extends RuntimeException {
        public NotSupportedMessageTypeException(String message) {
            super(message);
        }
    }

    public static class SlackException extends RuntimeException {
        public SlackException(String message) {
            super(message);
        }
    }

}
