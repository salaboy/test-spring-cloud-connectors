package org.salaboy.streams.connectors;

import org.springframework.cloud.service.UriBasedServiceInfo;

public class PaymentServiceInfo extends UriBasedServiceInfo {
    public static final String URI_SCHEME = "payment";



    public PaymentServiceInfo(String id,
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

    public PaymentServiceInfo(String id,
                              String uriString) {
        super(id,
              uriString);
    }

    @Override
    public String getScheme() {
        return URI_SCHEME;
    }
}
