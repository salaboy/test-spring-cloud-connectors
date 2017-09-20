package org.salaboy.streams.connectors2;

import org.springframework.cloud.service.UriBasedServiceInfo;

public class PaymentService2Info extends UriBasedServiceInfo {
    public static final String URI_SCHEME = "payment";



    public PaymentService2Info(String id,
                               String scheme,
                               String host,
                               int port,
                               String username,
                               String password,
                               String path) {
        super(id,
              scheme,
              host,
              port,
              username,
              password,
              path);
    }

    public PaymentService2Info(String id,
                               String uriString) {
        super(id,
              uriString);
    }

    @Override
    public String getScheme() {
        return URI_SCHEME;
    }
}
