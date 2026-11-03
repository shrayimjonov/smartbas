package uz.davrbank.zalogipatekahubservice.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
@Slf4j
public class NetworkDataService implements NetworkServices {
    //
    @Override
    public String getClientIPv4Address(HttpServletRequest request) {

        String ipAddress = request.getHeader("X-Forwarded-For");

        final String unknown = "unknown";
        if (StringUtils.isEmpty(ipAddress) || unknown.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ipAddress) || unknown.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ipAddress) || unknown.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            final String localhostIpv4 = "127.0.0.1";
            final String localhostIpv6 = "0:0:0:0:0:0:0:1";
            if (localhostIpv4.equals(ipAddress) || localhostIpv6.equals(ipAddress)) {
                try {
                    final InetAddress inetAddress = InetAddress.getLocalHost();
                    ipAddress = inetAddress.getHostAddress();
                } catch (UnknownHostException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        if (!StringUtils.isEmpty(ipAddress)
                && ipAddress.length() > 15
                && ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }

        return ipAddress;
    }

    @Override
    public String getClientHostAddress(HttpServletRequest requestHost) {
        String computerName = null;
        final String remoteAddress = requestHost.getRemoteAddr();

        try {
            final InetAddress inetAddress = InetAddress.getByName(remoteAddress);
            computerName = inetAddress.getHostName();
            if (computerName.equalsIgnoreCase("localhost")) {
                computerName = InetAddress.getLocalHost().getCanonicalHostName();
            }
        } catch (UnknownHostException e) {
            log.error("UnknownHostException detected in StartAction. ", e);
        }

        assert computerName != null;
        if (!StringUtils.trimWhitespace(computerName).isEmpty()) computerName = computerName.toUpperCase();
        return computerName;
    }

    public String getRemoteUserInfo(HttpServletRequest requestRemoteUser) {
        if (requestRemoteUser.getHeader("X-Forwarded-Host") != null) {
            return requestRemoteUser.getHeader("X-Forwarded-Host");
        } else {
            return requestRemoteUser.getServerName();
        }
    }
}