package ru.tigran.WebEditor.codeprocessor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.tigran.WebEditor.codeprocessor.implementation.CPPCodeProcessor;
import ru.tigran.WebEditor.codeprocessor.implementation.CSharpCodeProcessor;
import ru.tigran.WebEditor.codeprocessor.implementation.JavaCodeProcessor;
import ru.tigran.WebEditor.controller.dto.ProblemData;
import ru.tigran.WebEditor.controller.dto.TestResult;
import ru.tigran.WebEditor.database.redis.entity.Test;
import ru.tigran.WebEditor.others.F;
import ru.tigran.WebEditor.others.IProcessCreator;
import ru.tigran.WebEditor.others.IResponseEntityConverter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class CodeProcessor implements IResponseEntityConverter<String>, IProcessCreator {
    protected StatusCode code;
    protected String errorMessage = null;
    protected String directory;
    protected HashMap<String, TestResult> tests = new HashMap<>();
    protected List<String> outputs = new LinkedList<>();
    private boolean isSubmitMode = false;

    protected CodeProcessor() {
        directory = String.format("output\\%s", UUID.randomUUID());
        new File(directory).mkdirs();
        try {
            new File(String.format("%s\\in.txt", directory)).createNewFile();
            new File(String.format("%s\\out.txt", directory)).createNewFile();
            new File(String.format("%s\\err.log", directory)).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResponseEntity<String> run() {
        compile();
        executeSingle();
        ResponseEntity<String> result = toResponseEntity();
        clear();
        return result;
    }

    public ResponseEntity<String> run(Iterable<Test> tests) {
        isSubmitMode = true;
        compile();
        tests.forEach(this::executeTest);
        ResponseEntity<String> result = toResponseEntity();
        clear();
        return result;
    }

    public ResponseEntity<String> runTestMode(String[] tests) {
        compile();
        for (String test : tests) executeTest(test);
        ResponseEntity<String> result = toResponseEntityTest();
        clear();
        return result;
    }

    public boolean testsPassed() {
        return tests.values().stream().allMatch(TestResult::isPass);
    }

    @Override
    public ResponseEntity<String> toResponseEntity() {
        try {
            if (isSubmitMode && code != StatusCode.COMPILATION_ERROR)
                return new ResponseEntity<>(getTests(), HttpStatus.OK);
            if (code == StatusCode.EXECUTION_SUCCESS)
                return new ResponseEntity<>(String.format("\"%s\"", getOutput()), HttpStatus.OK);
            return new ResponseEntity<>(getErrorMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> toResponseEntityTest() {
        if (code == StatusCode.COMPILATION_ERROR)
            return new ResponseEntity<>(getErrorMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(getOutputs(), HttpStatus.OK);
    }

    private String getTests() {
        try {
            return new ObjectMapper().writeValueAsString(tests.values());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getOutputs() {
        try {
            return new ObjectMapper().writeValueAsString(outputs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String getDirectory() {
        return directory;
    }

    protected abstract Extension getExtension();

    protected abstract String[] getCompilationCommand();

    protected abstract String[] getExecutionCommand();

    protected void writeCode(String code) throws IOException {
        File file = new File(String.format("%s\\solution.%s", directory, getExtension().get()));
        if (file.createNewFile()) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(code);
            writer.close();
        }
    }

    private void compile() {
        try {
            Process process = createProcess(getCompilationCommand());
            if (!process.waitFor(20, TimeUnit.SECONDS)) {
                process.destroy();
                errorMessage = "?????????????????????? ???????????????????? ?????????? ???????????????????? ??????????????????!";
                code = StatusCode.COMPILATION_TIMEOUT;
                return;
            }
            process.destroy();
            code = initErrorMessage(process.exitValue()) ? StatusCode.COMPILATION_ERROR : StatusCode.COMPILATION_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
            code = StatusCode.COMPILATION_ERROR;
        }
    }

    private void executeSingle() {
        if (code == StatusCode.COMPILATION_ERROR || code == StatusCode.COMPILATION_TIMEOUT) return;
        try {
            Process process = createProcess(getExecutionCommand());
            if (!process.waitFor(3, TimeUnit.SECONDS)) {
                process.destroy();
                errorMessage = "?????????????????????? ???????????????????? ?????????? ???????????????????? ??????????????????!";
                code = StatusCode.EXECUTION_TIMEOUT;
                return;
            }
            process.destroy();
            code = initErrorMessage(process.exitValue()) ? StatusCode.EXECUTION_ERROR : StatusCode.EXECUTION_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
            code = StatusCode.EXECUTION_ERROR;
        }
    }

    private void executeTest(Test test) {
        if (code == StatusCode.COMPILATION_ERROR || code == StatusCode.COMPILATION_TIMEOUT) return;
        try {
            writeTest(test);
            executeSingle();
            if (code != StatusCode.EXECUTION_SUCCESS)
                tests.put(test.getId(), new TestResult(false, getErrorMessage()));
            else if (test.Equal(getOutput()))
                tests.put(test.getId(), new TestResult(true, "?????????????? ??????????"));
            else
                tests.put(test.getId(), new TestResult(false, "???????????????? ??????????"));
        } catch (IOException e) {
            e.printStackTrace();
            tests.put(test.getId(), new TestResult(false, e.getMessage()));
        }
    }

    private void executeTest(String input) {
        if (code == StatusCode.COMPILATION_ERROR || code == StatusCode.COMPILATION_TIMEOUT) return;
        try {
            writeInput(input);
            executeSingle();
            if (code != StatusCode.EXECUTION_SUCCESS) outputs.add(getErrorMessage());
            else outputs.add(getOutput());
        } catch (IOException e) {
            e.printStackTrace();
            outputs.add(e.getMessage());
        }
    }

    private void writeTest(Test test) throws IOException {
        File in = new File(String.format("%s\\in.txt", directory));
        FileOutputStream os = new FileOutputStream(in, false);
        os.write(test.getInput().getBytes());
        os.close();
    }

    private void writeInput(String input) throws IOException {
        File in = new File(String.format("%s\\in.txt", directory));
        FileOutputStream os = new FileOutputStream(in, false);
        os.write(input.getBytes());
        os.close();
    }

    private String getOutput() throws IOException {
        File error = new File(String.format("%s\\out.txt", directory));
        BufferedReader reader = new BufferedReader(new FileReader(error));
        String result = reader.lines().collect(Collectors.joining("\n"));
        reader.close();
        return result;
    }

    private String getErrorMessage() {
        String pattern;
        switch (code) {
            case COMPILATION_ERROR:
            case COMPILATION_TIMEOUT:
                pattern = "???????????? ????????????????????:\n%s";
                break;
            case EXECUTION_ERROR:
            case EXECUTION_TIMEOUT:
                pattern = "???????????? ???? ?????????? ????????????????????:\n%s";
                break;
            default:
                pattern = "%s";
        }
        return String.format(pattern, errorMessage.trim());
    }

    private void clear() {
        try {
            Files.walk(Path.of(directory))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean initErrorMessage(int exitCode) {
        boolean hasError = false;
        String error = getFile("err.log");
        if (error.equals("") && exitCode != 0) {
            hasError = true;
            errorMessage = getFile("out.txt");
            if (getExtension() == Extension.CSHARP)
                errorMessage = errorMessage.substring(F.nthIndexOf(errorMessage, "\n", 8) + 2);
        }
        if (!error.equals("")) {
            hasError = true;
            errorMessage = error;
        }
        return hasError;
    }

    protected String getFile(String name) {
        try {
            File error = new File(String.format("%s\\%s", directory, name));
            BufferedReader reader = new BufferedReader(new FileReader(error));
            List<String> lines = reader.lines().collect(Collectors.toList());
            reader.close();
            return String.join("\n", lines);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static CodeProcessor of(ProblemData problemData) throws IOException {
        CodeProcessor processor = instantiateProcessor(problemData.getLanguage());
        if (processor == null) return null;
        processor.writeCode(problemData.getCode());
        if (problemData.getInput() != null) processor.writeInput(problemData.getInput());
        return processor;
    }

    public static ResponseEntity<String> NoLanguageResponse(String language) {
        return new ResponseEntity<>(String.format("???????? %s ???? ????????????????????????????", language), HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<String> NoTaskResponse(int taskId) {
        return new ResponseEntity<>(String.format("???????????? ?? id=%d ???? ??????????????", taskId), HttpStatus.BAD_REQUEST);
    }

    private static CodeProcessor instantiateProcessor(String language) {
        switch (language) {
            case "cpp":
                return new CPPCodeProcessor();
            case "csharp":
                return new CSharpCodeProcessor();
            case "java":
                return new JavaCodeProcessor();
        }
        return null;
    }
}
