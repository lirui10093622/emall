package org.emall.user.api.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import org.emall.common.request.EmallRequest;

@Data
public class RegisterRequest extends EmallRequest {
    private Long orderId;
    private Long userId;
    private String useDate;
    private String contractName;
    private String contractPhone;
    private List<OrderItem> orderItems;

    @Data
    public static class OrderItem {
        private Long itemId;
        private Long productId;
        private Long vendorId;
        private Integer quantity;
        private BigDecimal basePrice;
        private BigDecimal salePrice;
        private List<Passenger> passengers;
    }

    @Data
    public static class Passenger {
        private Long id;
        private String name;
        private String idType;
        private String idNo;
        private String phone;
        private String email;
    }
}