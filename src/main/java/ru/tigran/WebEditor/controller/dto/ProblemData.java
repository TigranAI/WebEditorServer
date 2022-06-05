package ru.tigran.WebEditor.controller.dto;

public class ProblemData {
    private String code;
    private String language;
    private String input;
    private Integer taskId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "ProblemData{" +
                "code='" + code + '\'' +
                ", language='" + language + '\'' +
                ", input='" + input + '\'' +
                ", taskId=" + taskId +
                '}';
    }
}
