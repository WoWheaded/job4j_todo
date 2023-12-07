package ru.job4j.todo.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
public class HibernateCategoryRepository implements CategoryRepository {

    private final CrudRepository crudRepository;

    public HibernateCategoryRepository(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public Collection<Category> getAllCategories() {
        return crudRepository.query("FROM Category", Category.class);
    }

    @Override
    public Set<Category> findCategoriesById(Set<Integer> ids) {
        return new HashSet<>(crudRepository.query(
                "from Category WHERE id in :ids", Category.class,
                Map.of("ids", ids)));
    }
}
