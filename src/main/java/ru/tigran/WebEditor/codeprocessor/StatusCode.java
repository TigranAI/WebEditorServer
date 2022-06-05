package ru.tigran.WebEditor.codeprocessor;

public enum StatusCode {
    COMPLETE(200),
    COMPILATION_SUCCESS(201),
    EXECUTION_SUCCESS(202),
    COMPILATION_ERROR(400),
    COMPILATION_TIMEOUT(401),
    EXECUTION_TIMEOUT(402),
    EXECUTION_ERROR(402);

    private final int val;

    StatusCode(int val) {
        this.val = val;
    }

    public int get() {
        return val;
    }
}
