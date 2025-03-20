package com.owlexpress.hub.common;

public class Constant {
    public static class ErrorMessage{
        public static final String METHOD_ARGUMENT_NOT_VALID = "MethodArgumentNotValidException = {}";
        public static final String CONSTRAINT_VIOLATION_EXCEPTION = "ConstraintViolationException = {}";
        public static final String HUB_NOT_FOUND_EXCEPTION = "HubNotFoundException = {}";
        public static final String HUB_PRODUCT_NOT_FOUND_EXCEPTION = "HubProductNotFoundException = {}";

        public static final String HUB_DOES_NOT_MATCHES = "해당 허브를 찾을 수 없습니다.";
        public static final String ROUTE_NOT_FOUND_EXCEPTION = "해당 라우트를 찾을 수 없습니다.";


        public static final String HUB_INTERVAL_INFO_LOCATION_NOT_EXIST = "HUB_INTERVAL_INFO_LOCATION_NOT_EXIST = {}";
    }

    public static class ResponseMessage{

        public static final String HUB_FIND_SUCCESS = "허브 조회 성공";
        public static final String HUB_CREATE_SUCCESS = "허브 등록 완료";
        public static final String HUB_UPDATE_SUCCESS = "허브 수정 완료";
        public static final String HUB_SEARCH_SUCCESS = "허브 검색 성공";

        public static final String HUB_PRODUCT_FIND_SUCCESS = "허브 상품 조회 성공";
        public static final String HUB_PRODUCT_CREATE_SUCCESS = "허브 상품 추가 성공";
        public static final String HUB_PRODUCT_UPDATE_SUCCESS = "허브 상품 수정 성공";
        public static final String HUB_PRODUCT_SEARCH_SUCCESS = "허브 상품 검색 성공";
    }
}
