package ru.tigran.WebEditor.database.postgres.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tigran.WebEditor.database.postgres.entity.Problem;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {
}
