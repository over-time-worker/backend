package com.owlexpress.hub.common.exception;

public class HubProductException {

    public static class HubProductNotFoundException extends RuntimeException {

        public HubProductNotFoundException() {
            super("허브 상품이 존재하지 않습니다.");

        }
    }
}
