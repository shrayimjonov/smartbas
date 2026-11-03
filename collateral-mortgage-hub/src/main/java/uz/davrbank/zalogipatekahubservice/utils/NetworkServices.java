package uz.davrbank.zalogipatekahubservice.utils;

import jakarta.servlet.http.HttpServletRequest;

public interface NetworkServices {
    //
    String getClientIPv4Address(HttpServletRequest request);

    String getClientHostAddress(HttpServletRequest httpServletRequest);
}
