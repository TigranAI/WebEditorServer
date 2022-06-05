package ru.tigran.WebEditor.others;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public interface IProcessCreator {
    default Process createProcess(String... commands) throws IOException {
        File error = new File(String.format("%s\\err.log", getDirectory()));
        error.delete();
        error.createNewFile();
        File input = new File(String.format("%s\\in.txt", getDirectory()));
        File output = new File(String.format("%s\\out.txt", getDirectory()));
        output.delete();
        output.createNewFile();

        return new ProcessBuilder(commands)
                .redirectError(error)
                .redirectInput(input)
                .redirectOutput(output)
                .start();
    }

    String getDirectory();
}
