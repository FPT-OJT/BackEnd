package com.fpt.ojt.utils;

import com.fpt.ojt.exceptions.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class IpUtils {

    public String getClientIp() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        String[] headers = {"X-Forwarded-For", "X-Real-IP", "CF-Connecting-IP"};

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }
        var ip = request.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            throw new BadRequestException("IP address is not valid");
        }
        return ip;
    }
}
