package com.lrxun.lhttp.model;

/**
 * Created by
 * @author luopeng
 * @date 2019-11-30.
 * from Qidianyun company
 */
public enum HttpMethod {
    GET("GET"),

    POST("POST"),

    PUT("PUT"),

    DELETE("DELETE"),

    HEAD("HEAD"),

    PATCH("PATCH"),

    OPTIONS("OPTIONS"),

    TRACE("TRACE");

    private final String value;

    HttpMethod(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public boolean hasBody() {
        switch (this) {
            case POST:
            case PUT:
            case PATCH:
            case DELETE:
            case OPTIONS:
                return true;
            default:
                return false;
        }
    }
}
