package com.owlexpress.delivery.common.exception;

public class ExceptionMessage {
    public static final String DELIVERY_NOT_FOUND_MESSAGE = "일치 하는 배송 정보가 없습니다.";
    public static final String DELIVERY_HISTORY_NOT_FOUND_MESSAGE = "일치 하는 배송 경로 정보가 없습니다.";
    public static final String RETRY_MESSAGE = "잠시 후에 다시 시도해주세요.";
    public static final String DELIVERY_CREATE_FAIL_MESSAGE = "배송 생성에 실패했습니다.";
    public static final String DELIVERY_DELETE_FAIL_MESSAGE = "배송 취소에 실패했습니다. 이미 배송이 시작되었습니다.";
    public static final String DELIVERY_UPDATE_FAIL_MESSAGE = "배송 수정에 실패했습니다.";
    public static final String DELIVERY_MANAGER_ASSIGN_FAIL_MESSAGE = "배송 담당자 배정에 실패했습니다.";

}
