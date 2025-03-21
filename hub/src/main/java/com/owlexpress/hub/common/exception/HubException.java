package com.owlexpress.hub.common.exception;

import com.owlexpress.hub.common.Constant.ErrorMessage;

public class HubException {

    public static class HubNotFoundException extends RuntimeException {

        public HubNotFoundException() {
            super(ErrorMessage.HUB_DOES_NOT_MATCHES);
        }
    }

    public static class RouteNotFoundException extends Throwable {
        public RouteNotFoundException() {
            super(ErrorMessage.ROUTE_NOT_FOUND_EXCEPTION);
        }
    }

    public static class HubProductNotFoundException extends RuntimeException {
        public HubProductNotFoundException() {
            super(ErrorMessage.HUB_PRODUCT_NOT_FOUND_EXCEPTION);
        }
    }
}
