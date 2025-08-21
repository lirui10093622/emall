package org.emall.user.support;

import org.springframework.stereotype.Component;

@Component
public class ConfigHandler {

    public String getConnectNo(Long vendorId) {
        return vendorId.toString();
    }
}