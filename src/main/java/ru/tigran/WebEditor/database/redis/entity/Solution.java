package ru.tigran.WebEditor.database.redis.entity;

import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Solution")
public class Solution implements Serializable {
    private String id;
    private String code;
    private int taskId;
    public Solution() {
    }

    public Solution(String id, String code, int taskId) {
        this.id = id;
        this.code = code;
        this.taskId = taskId;
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
}
