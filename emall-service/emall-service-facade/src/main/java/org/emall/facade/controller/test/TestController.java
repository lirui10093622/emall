package org.emall.facade.controller.test;

import lombok.extern.slf4j.Slf4j;
import org.emall.common.response.EmallResponse;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Li Rui
 * @date 2025-08-29
 */
@Slf4j
@RestController
@RequestMapping("api/test")
public class TestController {

    @RequestMapping("testMDC")
    public EmallResponse<Void> testMDC() {
        MDC.put("rootTraceId", "0000");
        MDC.put("parentTraceId", "1111");
        MDC.put("traceId", "2222");
        log.info("test");
        return EmallResponse.success();
    }
}