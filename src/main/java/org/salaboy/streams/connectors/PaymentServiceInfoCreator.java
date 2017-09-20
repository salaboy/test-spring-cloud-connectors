package org.salaboy.streams.connectors;

import org.springframework.cloud.localconfig.LocalConfigServiceInfoCreator;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.UriBasedServiceData;

public class PaymentServiceInfoCreator extends LocalConfigServiceInfoCreator {

    @Override
    public ServiceInfo createServiceInfo(String id,
                                         String uri) {
        return new PaymentServiceInfo(id,
                                      uri);
    }

    @Override
    public boolean accept(Object o) {
        System.out.println("accept this o??? " + o);
        return true;
    }

    @Override
    public ServiceInfo createServiceInfo(Object o) {

        return new PaymentServiceInfo(((UriBasedServiceData) o).getKey(),
                                      ((UriBasedServiceData) o).getUri());
    }
}
