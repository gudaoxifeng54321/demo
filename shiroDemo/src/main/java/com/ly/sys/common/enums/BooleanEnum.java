package com.ly.sys.common.enums;

/**
 * <p>User: mtwu
 * <p>Date: 13-2-7 上午11:44
 * <p>Version: 1.0
 */
public enum BooleanEnum {
    T(1, "是"), F(0, "否");

    private final Integer value;
    private final String info;

    private BooleanEnum(Integer value, String info) {
        this.value = value;
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public Integer getValue() {
        return value;
    }
}
