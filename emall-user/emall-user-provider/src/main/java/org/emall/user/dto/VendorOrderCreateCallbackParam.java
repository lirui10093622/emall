package org.trip.user.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class VendorOrderCreateCallbackParam implements Serializable {
    private Long vendorId;
    private String vendorOrderId;
    private String status;
    private String message;
    private List<VendorOrderCreatePassenger> passengers;

    @Data
    public static class VendorOrderCreatePassenger implements Serializable {
        private String passengerId;
        private String voucherType;
        private String voucherData;
    }
}