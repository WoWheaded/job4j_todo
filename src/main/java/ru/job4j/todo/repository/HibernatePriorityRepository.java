package ru.job4j.todo.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;

import java.util.List;

@Repository
public class HibernatePriorityRepository implements PriorityRepository {
    private final CrudRepository crudRepository;

    public HibernatePriorityRepository(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public List<Priority> findAllPriority() {
        return crudRepository.query("FROM Priority AS p ORDER BY p.id", Priority.class);
    }

}
