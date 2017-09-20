package org.salaboy.streams.connectors;

import org.salaboy.streams.PaymentService;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreator;
import org.springframework.cloud.service.ServiceInfo;

public class PaymentServiceConnectorCreator implements ServiceConnectorCreator {

    @Override
    public Object create(ServiceInfo serviceInfo,
                         ServiceConnectorConfig serviceConnectorConfig) {
        return new PaymentServiceImpl();
    }

    @Override
    public Class getServiceConnectorType() {
        return PaymentService.class;
    }

    @Override
    public Class<?> getServiceInfoType() {
        return PaymentServiceInfo.class;
    }
}
