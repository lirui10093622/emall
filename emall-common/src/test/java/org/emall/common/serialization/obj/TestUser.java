package org.emall.common.serialization.obj;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Li Rui
 * @date 2025-09-11
 */
@Data
public class TestUser {
    private Long id;
    private String name;
    private Map<String, List<TestOrder>> orders;
    private List<String> logs;

    @Data
    public static class TestOrder {
        private Long id;
        private Date orderTime;
        private BigDecimal totalPrice;
        private List<TestOrderItem> items;
    }

    @Data
    public static class TestOrderItem {
        private Long id;
        private Long productId;
        private Integer quantity;
        private BigDecimal price;
        private String address;
    }
}