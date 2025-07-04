package org.emall.user.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
@Table(name = "t_user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 任务类型：CREATE, CANCEL
    private String taskType;
    private Long orderId;
    private Long orderItemId;
    private Long productId;
    private Long vendorId;
    private String connectNo;
    private Long userId;
    private String useDate;
    private String contractName;
    private String contractPhone;
    // 份数
    private Integer quantity;
    // 底价
    private BigDecimal basePrice;
    // 卖价
    private BigDecimal salePrice;

    // 处理次数
    private Integer processCount;
    // 处理状态：WAITING, PROCESSING, PROCESS_SUCCEED, PROCESS_FAILED
    private Integer processStatus;
    // 订单状态：NEW, WAITING_CONFIRMED, SUCCEED, FAILED, EXCEPTION, TIMEOUT
    private String orderStatus;
    // 通知状态：NO, PROCESSING, NOTIFIED
    private String notifyStatus;

    private String vendorOrderId;

    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;
}