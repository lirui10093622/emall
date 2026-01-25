package org.emall.facade.controller.app;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.emall.cart.api.EmallCartService;
import org.emall.common.api.EmallService;
import org.emall.common.api.dto.HealthDto;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.facade.vo.HealthVO;
import org.emall.order.api.EmallOrderService;
import org.emall.pay.api.EmallPayService;
import org.emall.product.api.EmallProductService;
import org.emall.search.api.EmallSearchService;
import org.emall.sms.api.EmallSmsService;
import org.emall.user.api.EmallUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Li Rui
 * @date 2025-09-03
 */
@Slf4j
@RestController
@RequestMapping("api/app")
public class AppController {
    @DubboReference
    private EmallCartService emallCartService;
    @DubboReference
    private EmallOrderService emallOrderService;
    @DubboReference
    private EmallPayService emallPayService;
    @DubboReference
    private EmallProductService emallProductService;
    @DubboReference
    private EmallSearchService emallSearchService;
    @DubboReference
    private EmallSmsService emallSmsService;
    @DubboReference
    private EmallUserService emallUserService;

    @RequestMapping("health")
    public EmallResponse<List<HealthVO>> health() {
        List<HealthVO> list = new LinkedList<>();
        addHealthVO(list, emallCartService);
        addHealthVO(list, emallOrderService);
        addHealthVO(list, emallPayService);
        addHealthVO(list, emallProductService);
        addHealthVO(list, emallSearchService);
        addHealthVO(list, emallSmsService);
        addHealthVO(list, emallUserService);
        return EmallResponse.success(list);
    }

    private void addHealthVO(List<HealthVO> list, EmallService emallService) {
        EmallResponse<HealthDto> response = emallService.healthCheck(new EmallRequest<>());
        if (response.isSuccess()) {
            list.add(HealthVO.from(response.getData()));
        }
    }
}