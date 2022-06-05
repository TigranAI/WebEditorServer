package ru.tigran.WebEditor.database.redis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.tigran.WebEditor.database.redis.entity.Test;

@Repository
public interface TestRepository extends CrudRepository<Test, String> {
}
