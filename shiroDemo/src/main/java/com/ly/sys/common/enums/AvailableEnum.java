/**
 * 
 *
 * 
 */
package com.ly.sys.common.enums;

/**
 * <p>User: mtwu
 * <p>Date: 13-2-7 上午11:44
 * <p>Version: 1.0
 */
public enum AvailableEnum {
    TRUE(1, "可用"), FALSE(0, "不可用");

    private final Integer value;
    private final String info;

    private AvailableEnum(Integer value, String info) {
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
