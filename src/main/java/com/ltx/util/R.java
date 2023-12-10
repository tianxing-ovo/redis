package com.ltx.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class R implements Serializable {

    private Boolean success;
    private String errorMsg;
    private Object data;
    private Long total;

    public static R ok() {
        return new R(true, null, null, null);
    }

    public static R ok(Object data) {
        return new R(true, null, data, null);
    }

    public static R ok(List<?> data, Long total) {
        return new R(true, null, data, total);
    }

    public static R fail(String errorMsg) {
        return new R(false, errorMsg, null, null);
    }
}
