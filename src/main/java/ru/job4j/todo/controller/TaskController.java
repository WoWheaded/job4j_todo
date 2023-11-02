package ru.job4j.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.HibernatePriorityRepository;
import ru.job4j.todo.service.HibernateTaskService;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final HibernateTaskService hibernateTaskService;
    private final HibernatePriorityRepository hibernatePriorityRepository;

    @Autowired
    public TaskController(HibernateTaskService hibernateTaskService, HibernatePriorityRepository hibernatePriorityRepository) {
        this.hibernateTaskService = hibernateTaskService;
        this.hibernatePriorityRepository = hibernatePriorityRepository;
    }

    @GetMapping("/create")
    public String getCreateTaskPage(Model model) {
        model.addAttribute("priorities", hibernatePriorityRepository.findAllPriority());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute Task task, @SessionAttribute User user) {
        task.setUser(user);
        hibernateTaskService.createTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}")
    public String getTaskById(@PathVariable int id, Model model) {
        model.addAttribute("priorities", hibernatePriorityRepository.findAllPriority());
        var taskById = hibernateTaskService.findTaskById(id);
        if (taskById.isEmpty()) {
            model.addAttribute("message", "Задача с идентификатором id: " + id + " не найдена");
            return "errors/404";
        }
        model.addAttribute("task", taskById.get());
        return "tasks/one";
    }

    @GetMapping()
    public String getAllTask(Model model) {
        model.addAttribute("tasks", hibernateTaskService.findAllTask());
        return "tasks/list";
    }

    @GetMapping("/onlyNew")
    public String getNewTask(Model model) {
        model.addAttribute("tasks", hibernateTaskService.findTaskByStatus(false));
        return "tasks/list";
    }

    @GetMapping("/onlyDone")
    public String getDoneTask(Model model) {
        model.addAttribute("tasks", hibernateTaskService.findTaskByStatus(true));
        return "tasks/list";
    }

    @GetMapping("/edit/{id}")
    public String editTaskPage(@PathVariable int id, Model model) {
        var taskById = hibernateTaskService.findTaskById(id);
        if (taskById.isEmpty()) {
            model.addAttribute("message", "Задача с указанным идентификатором id: " + id + "  не обновлена");
            return "errors/404";
        }
        model.addAttribute("task", taskById.get());
        return "tasks/edit";
    }

    @PostMapping("/update")
    public String updateTask(@ModelAttribute Task task, Model model, @SessionAttribute User user) {
        task.setUser(user);
        var isUpdated = hibernateTaskService.updateTask(task);
        if (!isUpdated) {
            model.addAttribute("message", "Задача с идентификатором id: " + task.getId() + " не обновлена");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/setDoneStatus/{id}")
    public String setDoneStatusTask(@PathVariable int id, Model model) {
        var taskById = hibernateTaskService.updateTaskStatus(id);
        if (!taskById) {
            model.addAttribute("message", "Задача с идентификатором id: " + id + " не обновлена");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String deleteTaskById(@PathVariable int id, Model model) {
        var isDeleted = hibernateTaskService.deleteTaskById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Задача с идентификатором id: " + id + " не удалена");
            return "errors/404";
        }
        return "redirect:/tasks";
    }
}
