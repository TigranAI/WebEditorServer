package ru.tigran.WebEditor.database.postgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.tigran.WebEditor.database.postgres.entity.Problem;
import ru.tigran.WebEditor.database.postgres.repository.ProblemRepository;

@Component
public class InitDatabase {
    @Autowired
    private ProblemRepository problemRepository;

    @EventListener
    public void onLoad(ApplicationReadyEvent e){
        if (problemRepository.count() == 0) {
            var problem = new Problem();
            problem.setTitle("Умножение чисел");
            problem.setDescription("multiply_nums.html");
            problem.setDifficulty(1);
            problem.setTests(new String[] {"multiply_nums1", "multiply_nums2", "multiply_nums3"});
            problemRepository.save(problem);

            var problem2 = new Problem();
            problem2.setTitle("Деление на 0");
            problem2.setDescription("zero_division.html");
            problem2.setDifficulty(1);
            problem2.setTests(new String[] {"zero_division1", "zero_division2", "zero_division3"});
            problemRepository.save(problem2);
        }
    }
}
