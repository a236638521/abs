package com.m7.abs.reportexport.nsq;

import com.fasterxml.jackson.core.type.TypeReference;
import com.m7.abs.common.annotation.NsqListener;
import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.domain.dto.MiddleNumberCdrReport;
import com.m7.abs.common.domain.dto.NsqMsgDTO;
import com.m7.abs.common.utils.FastJsonUtils;
import com.m7.abs.reportexport.common.utils.JsonComponent;
import com.m7.abs.reportexport.service.IMiddleNumberCdrReportService;
import com.sproutsocial.nsq.Message;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 小号话单报表
 *
 * @author Kejie Peng
 * @date 2023年 03月28日 11:42:32
 */
@Slf4j
@Component
public class MidNumCdrReportCustomer {
    @Autowired
    protected JsonComponent jsonComponent;
    @Autowired
    private IMiddleNumberCdrReportService cdrReportService;

    @NsqListener(topic = "#{@absReportProperties.api.report.midNumCdrReportTopic}",
            channel = "#{@absReportProperties.api.report.midNumCdrReportChannel}")
    private void subscribeMidNumCdrMsg(Message message) {
        try {
            byte[] data = message.getData();
            NsqMsgDTO<MiddleNumberCdrReport> msg = null;
            try {
                TypeReference<NsqMsgDTO<MiddleNumberCdrReport>> typeReference = new TypeReference<NsqMsgDTO<MiddleNumberCdrReport>>() {
                };
                msg = jsonComponent.readValue(data, typeReference);
            } catch (Exception e) {
                log.error("无法解析小号话单数据,退出队列: {}", new String(data), e);
                return;
            }
            if (msg == null) {
                log.warn("无法解析小号话单数据,消息为空", new String(data));
                return;
            }
            MDC.put(CommonSessionKeys.REQ_ID_KEY, msg.getTraceId());
            log.info("接收到小号话单,NSQ消息:{}", FastJsonUtils.toJSONString(msg));
            cdrReportService.handleMidNumCdrMsg(msg);
        } catch (Exception e) {
            log.error("push data NSQ error", e);
        } finally {
            message.finish();
            MDC.remove(CommonSessionKeys.REQ_ID_KEY);
        }
    }
}
