package org.salaboy.streams.model;

import java.util.Map;
import java.util.UUID;

public class IntegrationEvent {

    private String id;
    private String correlationId;
    private String processInstanceId;
    private String taskId;
    private String executionId;

    private Map<String, Object> variables;

    public IntegrationEvent() {
        this.id = UUID.randomUUID().toString();
    }

    public IntegrationEvent(String correlationId,
                            String processInstanceId,
                            String taskId,
                            String executionId) {
        this();
        this.correlationId = correlationId;
        this.processInstanceId = processInstanceId;
        this.taskId = taskId;
        this.executionId = executionId;
    }

    public String getId() {
        return id;
    }

    public IntegrationEvent(String correlationId,
                            String processInstanceId,
                            String taskId,
                            String executionId,
                            Map<String, Object> variables) {
        this(correlationId,
             processInstanceId,
             taskId, executionId);
        this.variables = variables;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getExecutionId() {
        return executionId;
    }

    @Override
    public String toString() {
        return "IntegrationEvent{" +
                "id='" + id + '\'' +
                ", correlationId='" + correlationId + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", executionId='" + executionId + '\'' +
                ", variables=" + variables +
                '}';
    }
}
