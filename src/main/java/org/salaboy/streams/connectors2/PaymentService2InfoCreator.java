package org.salaboy.streams.connectors2;

import org.springframework.cloud.localconfig.LocalConfigServiceInfoCreator;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.UriBasedServiceData;

public class PaymentService2InfoCreator extends LocalConfigServiceInfoCreator {

    @Override
    public ServiceInfo createServiceInfo(String id,
                                         String uri) {
        return new PaymentService2Info(id,
                                       uri);
    }

    @Override
    public boolean accept(Object o) {
        System.out.println("accept this o??? " + o);
        return true;
    }

    @Override
    public ServiceInfo createServiceInfo(Object o) {

        return new PaymentService2Info(((UriBasedServiceData) o).getKey(),
                                       ((UriBasedServiceData) o).getUri());
    }
}
