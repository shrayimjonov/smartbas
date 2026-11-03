package uz.davrbank.absresourceaccesshubservice.util;

import jakarta.servlet.http.HttpServletRequest;

public interface NetworkServices {
    //
    String getClientIPv4Address(HttpServletRequest request);

    String getClientHostAddress(HttpServletRequest httpServletRequest);
}
