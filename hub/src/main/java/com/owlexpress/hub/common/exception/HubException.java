package com.owlexpress.hub.common.exception;

public class HubException {

    public static class HubNotFoundException extends RuntimeException {

        public HubNotFoundException() {
            super("해당 허브를 찾을 수 없습니다.");
        }
    }
}
