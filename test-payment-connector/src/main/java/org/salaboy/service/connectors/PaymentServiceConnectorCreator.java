package org.salaboy.service.connectors;

import org.salaboy.service.api.PaymentService;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreator;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.UriBasedServiceInfo;
import org.springframework.web.client.RestTemplate;

public class PaymentServiceConnectorCreator implements ServiceConnectorCreator {

    @Override
    public Object create(ServiceInfo serviceInfo,
                         ServiceConnectorConfig serviceConnectorConfig) {

        return new PaymentServiceImpl(new RestTemplate(),
                                      (UriBasedServiceInfo) serviceInfo);
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
