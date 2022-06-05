package ru.tigran.WebEditor.codeprocessor;

public enum Extension {
    CPP("cpp"),
    JAVA("java"),
    CSHARP("cs");

    private final String val;

    Extension(String val) {
        this.val = val;
    }

    public String get() {
        return val;
    }
}
