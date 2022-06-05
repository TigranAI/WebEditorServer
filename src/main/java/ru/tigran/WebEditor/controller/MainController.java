package ru.tigran.WebEditor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.tigran.WebEditor.codeprocessor.CodeProcessor;
import ru.tigran.WebEditor.controller.dto.ProblemData;
import ru.tigran.WebEditor.database.postgres.entity.Problem;
import ru.tigran.WebEditor.database.postgres.repository.ProblemRepository;
import ru.tigran.WebEditor.database.redis.entity.Solution;
import ru.tigran.WebEditor.database.redis.repository.SolutionRepository;
import ru.tigran.WebEditor.database.redis.repository.TestRepository;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/editor")
public class MainController {
    @Autowired
    ProblemRepository problemRepository;
    @Autowired
    TestRepository testRepository;
    @Autowired
    SolutionRepository solutionRepository;

    @GetMapping()
    public String index() {
        return "index";
    }

    @GetMapping("/monaco")
    public String get() {
        return "editor :: monaco";
    }

    @PostMapping("/run")
    @ResponseBody
    public ResponseEntity<String> runCode(@RequestBody ProblemData problemData) throws Exception {
        CodeProcessor processor = CodeProcessor.of(problemData);
        if (processor == null) return CodeProcessor.NoLanguageResponse(problemData.getLanguage());
        return processor.run();
    }

    @PostMapping("/submit")
    @ResponseBody
    public ResponseEntity<String> submitCode(@RequestBody ProblemData problemData) throws Exception {
        Optional<Problem> optional = problemRepository.findById(problemData.getTaskId());
        if (optional.isEmpty()) return CodeProcessor.NoTaskResponse(problemData.getTaskId());
        Problem problem = optional.get();
        CodeProcessor processor = CodeProcessor.of(problemData);
        if (processor == null) return CodeProcessor.NoLanguageResponse(problemData.getLanguage());
        ResponseEntity<String> result = processor.run(testRepository.findAllById(List.of(problem.getTests())));
        if (processor.testsPassed())
            solutionRepository.save(Solution.of(processor, problemData));
        return result;
    }

}
