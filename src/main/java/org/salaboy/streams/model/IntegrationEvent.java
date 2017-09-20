package org.salaboy.streams.model;

import java.util.Map;
import java.util.UUID;

public class IntegrationEvent {

    private String id;
    private String correlationId;
    private Map<String, Object> variables;

    public IntegrationEvent() {
        this.id = UUID.randomUUID().toString();
    }

    public IntegrationEvent(String correlationId) {
        this();
        this.correlationId = correlationId;
    }

    public String getId() {
        return id;
    }

    public IntegrationEvent(String correlationId,
                            Map<String, Object> variables) {
        this(correlationId);
        this.variables = variables;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    @Override
    public String toString() {
        return "IntegrationEvent{" +
                "id='" + id + '\'' +
                ", correlationId='" + correlationId + '\'' +
                ", variables=" + variables +
                '}';
    }
}
