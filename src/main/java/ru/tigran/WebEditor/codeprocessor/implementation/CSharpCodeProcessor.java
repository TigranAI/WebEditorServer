package ru.tigran.WebEditor.codeprocessor.implementation;

import ru.tigran.WebEditor.codeprocessor.CodeProcessor;
import ru.tigran.WebEditor.codeprocessor.Extension;

public class CSharpCodeProcessor extends CodeProcessor {
    @Override
    protected Extension getExtension() {
        return Extension.CSHARP;
    }

    @Override
    protected String[] getCompilationCommand() {
        return new String[]{
                "csc",
                "/utf8output",
                String.format("/out:%s\\solution.exe", directory),
                String.format("%s\\solution.cs", directory)
        };
    }

    @Override
    protected String[] getExecutionCommand() {
        return new String[] {
                String.format("%s\\solution.exe", directory)
        };
    }
}
