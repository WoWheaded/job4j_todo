package ru.job4j.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.repository.HibernatePriorityRepository;

import java.util.List;

@Service
public class HibernatePriorityService implements PriorityService {

    private final HibernatePriorityRepository hibernatePriorityRepository;

    @Autowired
    public HibernatePriorityService(HibernatePriorityRepository hibernatePriorityRepository) {
        this.hibernatePriorityRepository = hibernatePriorityRepository;
    }

    @Override
    public List<Priority> findAllPriority() {
        return hibernatePriorityRepository.findAllPriority();
    }
}
