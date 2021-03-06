package com.utils.service.camel.route;

import com.utils.service.camel.common.Constants;
import com.utils.service.camel.common.FlowRouteNames;
import com.utils.service.camel.processor.validation.common.GlobalExceptionProcessor;
import com.utils.service.camel.processor.validation.ReceiveSMSProcessor;
import com.utils.service.dto.sms.SendSMSRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class SMSRouteFlow extends RestRoute {

    final private ReceiveSMSProcessor receiveSMSProcessor;
    final private Constants constants;
    final private GlobalExceptionProcessor globalExceptionProcessor;

    public SMSRouteFlow(ReceiveSMSProcessor receiveSMSProcessor, Constants constants, GlobalExceptionProcessor globalExceptionProcessor) {
        this.receiveSMSProcessor = receiveSMSProcessor;
        this.constants = constants;
        this.globalExceptionProcessor = globalExceptionProcessor;
    }

    @Override
    public void configure() {
        onException(Exception.class).process(globalExceptionProcessor)
                .to(FlowRouteNames.AUDIT_ROUTE_NAME).handled(true);


        // Build end points Route
        buildEndpointRoute(constants.getSendSMSBusinessServiceName(),
                SendSMSRequestDTO.class,
                FlowRouteNames.SEND_SMS_SERVICE_ROUTE);

        buildGeneralRoute(FlowRouteNames.SEND_SMS_SERVICE_ROUTE,
                receiveSMSProcessor);
    }
}
