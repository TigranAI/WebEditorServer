package ru.tigran.WebEditor.codeprocessor.implementation;

import ru.tigran.WebEditor.codeprocessor.CodeProcessor;
import ru.tigran.WebEditor.codeprocessor.Extension;
import ru.tigran.WebEditor.others.F;

import java.io.IOException;

public class JavaCodeProcessor extends CodeProcessor {
    @Override
    protected Extension getExtension() {
        return Extension.JAVA;
    }

    @Override
    protected String[] getCompilationCommand() {
        return new String[] {
            "javac",
            String.format("%s\\solution.java", directory)
        };
    }

    @Override
    protected String[] getExecutionCommand() {
        return new String[] {
            "java",
            "-cp",
            directory,
            "solution"
        };
    }

    @Override
    protected void writeCode(String code) throws IOException {
        code = replaceClassname(code);
        super.writeCode(code);
    }

    private String replaceClassname(String code) {
        int mainIndex = code.indexOf("main");
        if (mainIndex == -1) return code;
        int classIndex = F.lastIndexOfTo(code, "class", mainIndex);
        if (classIndex == -1) return code;
        String className = getClassName(code, classIndex, mainIndex);
        return code.replaceAll(className, "solution");
    }

    private String getClassName(String code, int classKeyPos, int mainFuncPos) {
        return code.substring(classKeyPos + 5, mainFuncPos).trim().split(" ")[0];
    }
}
