package ru.job4j.todo.repository;

import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.Set;

public interface CategoryRepository {
    Collection<Category> getAllCategories();

    Set<Category> findCategoriesById(Set<Integer> ids);
}
