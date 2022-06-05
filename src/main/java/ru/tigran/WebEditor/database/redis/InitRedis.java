package ru.tigran.WebEditor.database.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.tigran.WebEditor.database.redis.entity.Test;
import ru.tigran.WebEditor.database.redis.repository.TestRepository;

import java.util.List;

@Component
public class InitRedis {
    @Autowired
    TestRepository testRepository;

    @EventListener
    public void onLoad(ApplicationReadyEvent e){
        if (testRepository.count() != 0) return;
        List<Test> tests = List.of(
                new Test("multiply_nums1", "5 10", "50"),
                new Test("multiply_nums2", "250 250", "62500"),
                new Test("multiply_nums3", "-1 30", "-30"),
                new Test("zero_division1", "10 5", "2"),
                new Test("zero_division2", "7 10", "0"),
                new Test("zero_division3", "5 0", "NO")
        );
        testRepository.saveAll(tests);
    }
}
