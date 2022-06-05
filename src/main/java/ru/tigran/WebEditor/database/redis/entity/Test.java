package ru.tigran.WebEditor.database.redis.entity;

import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Tests")
public class Test implements Serializable {
    private String id;
    private String input;
    private String output;

    public Test() {
    }

    public Test(String id, String input, String output) {
        this.id = id;
        this.input = input;
        this.output = output;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public boolean Equal(String output){
        return this.output.equals(output);
    }
}
