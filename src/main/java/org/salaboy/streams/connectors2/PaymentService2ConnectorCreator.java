package org.salaboy.streams.connectors2;

import org.salaboy.streams.PaymentService;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreator;
import org.springframework.cloud.service.ServiceInfo;

public class PaymentService2ConnectorCreator implements ServiceConnectorCreator {

    @Override
    public Object create(ServiceInfo serviceInfo,
                         ServiceConnectorConfig serviceConnectorConfig) {
        return new PaymentService2Impl();
    }

    @Override
    public Class getServiceConnectorType() {
        return PaymentService.class;
    }

    @Override
    public Class<?> getServiceInfoType() {
        return PaymentService2Info.class;
    }
}
