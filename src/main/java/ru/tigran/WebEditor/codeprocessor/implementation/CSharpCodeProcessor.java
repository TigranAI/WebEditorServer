package ru.tigran.WebEditor.codeprocessor.implementation;

import ru.tigran.WebEditor.codeprocessor.CodeProcessor;
import ru.tigran.WebEditor.codeprocessor.Extension;

import java.io.IOException;

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
                String.format("%s\\solution.exe", directory),
                "/utf8output"
        };
    }

    @Override
    protected void writeCode(String code) throws IOException {
        code = addEncoding(code);
        super.writeCode(code);
    }

    private String addEncoding(String code) {
        int mainIndex = code.indexOf("Main");
        if (mainIndex == -1) return code;
        int nextLineIndex = code.indexOf("{", mainIndex);
        if (nextLineIndex == -1) return code;
        return new StringBuilder(code)
                .insert(nextLineIndex + 1, "\nConsole.OutputEncoding = System.Text.Encoding.UTF8;")
                .toString();
    }
}
