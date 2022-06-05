package ru.tigran.WebEditor.codeprocessor.implementation;

import ru.tigran.WebEditor.codeprocessor.CodeProcessor;
import ru.tigran.WebEditor.codeprocessor.Extension;

public class JavaCodeProcessor extends CodeProcessor {
    @Override
    protected Extension getExtension() {
        return Extension.JAVA;
    }

    @Override
    protected String[] getCompilationCommand() {
        return new String[0];
    }

    @Override
    protected String[] getExecutionCommand() {
        return new String[0];
    }
}
