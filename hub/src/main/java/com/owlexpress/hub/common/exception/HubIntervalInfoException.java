package com.owlexpress.hub.common.exception;

import com.owlexpress.hub.common.Constant.ErrorMessage;

public class HubIntervalInfoException {

    public static class LocationNotExistException extends RuntimeException {

        public LocationNotExistException() {
            super(ErrorMessage.HUB_INTERVAL_INFO_LOCATION_NOT_EXIST);
        }
    }
}
