package org.lr.expriment.emall.order.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.emall.api.dto.HealthDto;
import org.emall.common.enums.AppStatusEnum;
import org.emall.common.exception.EmallException;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.order.api.EmallOrderService;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Li Rui
 * @date 2025-09-03
 */
@Slf4j
@DubboService
public class EmallOrderServiceImpl implements EmallOrderService {
    @Value("${spring.application.name}")
    private String appName;

    @Override
    public EmallResponse<HealthDto> healthCheck(EmallRequest<Void> request) throws EmallException {
        return EmallResponse.success(new HealthDto(appName, AppStatusEnum.RUNNING.name()));
    }

    @Override
    public EmallResponse<Void> createOrder(EmallRequest<Void> request) throws EmallException {
        return EmallResponse.success();
    }
}