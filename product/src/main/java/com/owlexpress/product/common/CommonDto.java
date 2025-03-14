package com.owlexpress.product.common;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonDto<T> {

    private HttpStatus status;
    private Integer code;
    private String message;
    private T data;

    @Builder
    public CommonDto(HttpStatus status, Integer code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
