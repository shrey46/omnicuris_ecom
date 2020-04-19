package com.ecom.beans;

import org.springframework.http.HttpStatus;
import lombok.Data;

/**
 * @author shrey
 */

@Data
public final class EcomApplicationResponse {
    private Object data;
    private boolean success;
    private int httpStatus;


    public static EcomApplicationResponse success(final Object data) {
        final EcomApplicationResponse response = new EcomApplicationResponse();
        response.setData(data);
        response.setSuccess(true);
        response.setHttpStatus(HttpStatus.OK.value());

        return response;
    }

    public static EcomApplicationResponse success() {
        final EcomApplicationResponse response = new EcomApplicationResponse();
        response.setSuccess(true);
        response.setHttpStatus(HttpStatus.OK.value());
        return response;
    }


}
