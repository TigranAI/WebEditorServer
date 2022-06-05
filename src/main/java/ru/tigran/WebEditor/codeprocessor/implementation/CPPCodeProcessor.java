package ru.tigran.WebEditor.codeprocessor.implementation;

import ru.tigran.WebEditor.codeprocessor.CodeProcessor;
import ru.tigran.WebEditor.codeprocessor.Extension;

public class CPPCodeProcessor extends CodeProcessor {
    @Override
    protected Extension getExtension() {
        return Extension.CPP;
    }

    @Override
    protected String[] getCompilationCommand() {
        return new String[]{
                "g++",
                String.format("%s\\solution.cpp", directory),
                "-o",
                String.format("%s\\solution.exe", directory)
        };
    }

    @Override
    protected String[] getExecutionCommand() {
        return new String[] {
                String.format("%s\\solution.exe", directory)
        };
    }
}
