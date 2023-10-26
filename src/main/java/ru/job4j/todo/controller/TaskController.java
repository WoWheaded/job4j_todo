package ru.job4j.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.HibernateTaskService;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final HibernateTaskService hibernateTaskService;

    @Autowired
    public TaskController(HibernateTaskService hibernateTaskService) {
        this.hibernateTaskService = hibernateTaskService;
    }

    @GetMapping("/create")
    public String getCreateTaskPage() {
        return "tasks/create";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute Task task) {
        task.setDone(false);
        hibernateTaskService.createTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}")
    public String getTaskById(@PathVariable int id, Model model) {
        var taskById = hibernateTaskService.findTaskById(id);
        if (taskById.isEmpty()) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
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
        model.addAttribute("tasks", hibernateTaskService.findNewTask());
        return "tasks/list";
    }

    @GetMapping("/onlyDone")
    public String getDoneTask(Model model) {
        model.addAttribute("tasks", hibernateTaskService.findDoneTask());
        return "tasks/list";
    }

    @GetMapping("/edit/{id}")
    public String editTaskPage(@PathVariable int id, Model model) {
        var taskById = hibernateTaskService.findTaskById(id);
        if (taskById.isEmpty()) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        model.addAttribute("task", taskById.get());
        return "tasks/edit";
    }

    @PostMapping("/update")
    public String updateTask(@ModelAttribute Task task) {
        hibernateTaskService.updateTask(task.getId(), task);
        return "redirect:/tasks";
    }

    @GetMapping("/setDoneStatus/{id}")
    public String setDoneStatusTask(@PathVariable int id) {
        var taskById = hibernateTaskService.findTaskById(id).get();
        taskById.setDone(true);
        hibernateTaskService.updateTask(id, taskById);
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String deleteTaskById(@PathVariable int id, Model model) {
        var isDeleted = hibernateTaskService.deleteTaskById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/tasks";
    }
}
