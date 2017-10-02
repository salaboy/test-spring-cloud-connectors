package org.salaboy.streams.model;

import java.util.Map;
import java.util.UUID;

public class IntegrationResult {

    private String id;
    private String correlationId;
    private Map<String, Object> variables;

    public IntegrationResult() {
        this.id = UUID.randomUUID().toString();
    }

    public IntegrationResult(String correlationId) {
        this();
        this.correlationId = correlationId;
    }

    public IntegrationResult(String correlationId,
                             Map<String, Object> variables) {
        this(correlationId);
        this.variables = variables;
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    @Override
    public String toString() {
        return "IntegrationResult{" +
                "id='" + id + '\'' +
                ", correlationId='" + correlationId + '\'' +
                ", variables=" + variables +
                '}';
    }
}
