package com.owlexpress.hub.common.exception;

public class HubProductException {

    public static class HubProductNotFoundException extends RuntimeException {

        public HubProductNotFoundException() {
            super("허브 상품이 존재하지 않습니다.");

        }

        public static class LockAcquisitionFailedException extends RuntimeException {

            public LockAcquisitionFailedException() {
                super("상품 재고 락 획득 실패");
            }
        }
    }

    public static class HubProductStockNotEnoughException extends RuntimeException {

        public HubProductStockNotEnoughException() {
            super("요청하신 제품들의 수량이 충분하지 않습니다.");
        }
    }
}
