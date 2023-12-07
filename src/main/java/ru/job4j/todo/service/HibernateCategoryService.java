package ru.job4j.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.repository.HibernateCategoryRepository;

import java.util.Collection;
import java.util.Set;

@Service
public class HibernateCategoryService implements CategoryService {

    private final HibernateCategoryRepository hibernateCategoryRepository;

    @Autowired
    public HibernateCategoryService(HibernateCategoryRepository hibernateCategoryRepository) {
        this.hibernateCategoryRepository = hibernateCategoryRepository;
    }

    @Override
    public Collection<Category> getAllCategories() {
        return hibernateCategoryRepository.getAllCategories();
    }

    @Override
    public Set<Category> findCategoriesById(Set<Integer> ids) {
        return hibernateCategoryRepository.findCategoriesById(ids);
    }
}
