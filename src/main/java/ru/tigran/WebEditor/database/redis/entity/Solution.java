package ru.tigran.WebEditor.database.redis.entity;

import org.springframework.data.redis.core.RedisHash;
import ru.tigran.WebEditor.codeprocessor.CodeProcessor;
import ru.tigran.WebEditor.controller.dto.ProblemData;

import java.io.Serializable;

@RedisHash("Solution")
public class Solution implements Serializable {
    private String id;
    private String code;
    private int taskId;
    private String language;

    public Solution() {
    }

    public Solution(String id, String code, int taskId) {
        this.id = id;
        this.code = code;
        this.taskId = taskId;
    }

    public static Solution of (CodeProcessor processor, ProblemData problemData) {
        Solution result = new Solution();
        result.setId(processor.getDirectory().split("\\\\")[1]);
        result.setTaskId(problemData.getTaskId());
        result.setLanguage(problemData.getLanguage());
        result.setCode(problemData.getCode());
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
