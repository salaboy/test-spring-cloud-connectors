package org.salaboy.service.kubernetes.connectors;

import io.fabric8.kubernetes.api.model.Service;
import org.springframework.cloud.kubernetes.connector.KubernetesServiceInfoCreator;

public class PaymentServiceInfoCreator extends KubernetesServiceInfoCreator<PaymentServiceInfo> {

    public PaymentServiceInfoCreator(String identifyingLabelValue) {
        super(identifyingLabelValue);
    }

    @Override
    public PaymentServiceInfo createServiceInfo(Service service) {
        int port = service.getSpec().getPorts().iterator().next().getPort();
        String clusterIP = service.getSpec().getClusterIP();

        return new PaymentServiceInfo("payment",
                                      "http://" + clusterIP + ":" + port + "/payment");
    }
}
