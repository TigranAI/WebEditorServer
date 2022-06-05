package ru.tigran.WebEditor.controller.dto;

import java.io.Serializable;

public class TestResult implements Serializable {
    private boolean pass;
    private String msg;

    public TestResult(boolean pass, String msg) {
        this.pass = pass;
        this.msg = msg;
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
