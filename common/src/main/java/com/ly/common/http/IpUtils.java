package com.ly.common.http;

import javax.servlet.http.HttpServletRequest;

public class IpUtils {
    private IpUtils() {
    }

    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            //没有被反向代理转发过的 请求头
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
