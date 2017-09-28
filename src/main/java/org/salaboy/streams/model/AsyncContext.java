package org.salaboy.streams.model;

public class AsyncContext {

    private String processInstanceId;
    private String taskId;
    private String executionId;

    public AsyncContext() {
    }

    public AsyncContext(String processInstanceId,
                        String taskId,
                        String executionId) {
        this.processInstanceId = processInstanceId;
        this.taskId = taskId;
        this.executionId = executionId;
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
}
