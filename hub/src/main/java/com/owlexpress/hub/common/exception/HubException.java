package com.owlexpress.hub.common.exception;

import com.owlexpress.hub.common.Constant.ErrorMessage;

public class HubException {

    public static class HubNotFoundException extends RuntimeException {

        public HubNotFoundException() {
            super(ErrorMessage.HUB_DOES_NOT_MATCHES);
        }
    }
}
