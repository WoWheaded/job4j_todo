package ru.job4j.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.HibernateCategoryService;
import ru.job4j.todo.service.HibernatePriorityService;
import ru.job4j.todo.service.HibernateTaskService;

import java.time.ZoneId;
import java.util.Collection;
import java.util.Set;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final HibernateTaskService hibernateTaskService;
    private final HibernatePriorityService hibernatePriorityService;
    private final HibernateCategoryService hibernateCategoryService;

    @Autowired
    public TaskController(HibernateTaskService hibernateTaskService, HibernatePriorityService hibernatePriorityService, HibernateCategoryService hibernateCategoryService) {
        this.hibernateTaskService = hibernateTaskService;
        this.hibernatePriorityService = hibernatePriorityService;
        this.hibernateCategoryService = hibernateCategoryService;
    }

    @GetMapping("/create")
    public String getCreateTaskPage(Model model) {
        model.addAttribute("priorities", hibernatePriorityService.findAllPriority());
        model.addAttribute("categories", hibernateCategoryService.getAllCategories());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute Task task, @SessionAttribute User user,
                             @RequestParam(required = false) Set<Integer> cIds) {
        task.setUser(user);
        if (!cIds.isEmpty()) {
            task.setCategories(hibernateCategoryService.findCategoriesById(cIds));
        }
        hibernateTaskService.createTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}")
    public String getTaskById(@PathVariable int id, Model model) {
        var taskById = hibernateTaskService.findTaskById(id);
        if (taskById.isEmpty()) {
            model.addAttribute("message", "Задача с идентификатором id: " + id + " не найдена");
            return "errors/404";
        }
        model.addAttribute("priorities", hibernatePriorityService.findAllPriority());
        model.addAttribute("task", taskById.get());
        return "tasks/one";
    }

    @GetMapping()
    public String getAllTask(Model model, @SessionAttribute User user) {
        model.addAttribute("tasks", convertTimeWithTimezone(hibernateTaskService.findAllTask(), user));
        return "tasks/list";
    }

    @GetMapping("/onlyNew")
    public String getNewTask(Model model, @SessionAttribute User user) {
        model.addAttribute("tasks", convertTimeWithTimezone(hibernateTaskService.findTaskByStatus(false), user));
        return "tasks/list";
    }

    @GetMapping("/onlyDone")
    public String getDoneTask(Model model, @SessionAttribute User user) {
        model.addAttribute("tasks", convertTimeWithTimezone(hibernateTaskService.findTaskByStatus(true), user));
        return "tasks/list";
    }

    @GetMapping("/edit/{id}")
    public String editTaskPage(@PathVariable int id, Model model) {
        var taskById = hibernateTaskService.findTaskById(id);
        if (taskById.isEmpty()) {
            model.addAttribute("message", "Задача с указанным идентификатором id: " + id + "  не обновлена");
            return "errors/404";
        }
        model.addAttribute("priorities", hibernatePriorityService.findAllPriority());
        model.addAttribute("task", taskById.get());
        model.addAttribute("categories", hibernateCategoryService.getAllCategories());
        return "tasks/edit";
    }

    @PostMapping("/update")
    public String updateTask(@ModelAttribute Task task, Model model, @SessionAttribute User user,
                             @RequestParam Set<Integer> cIds) {
        task.setUser(user);
        if (!cIds.isEmpty()) {
            task.setCategories(hibernateCategoryService.findCategoriesById(cIds));
        }
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

    private Collection<Task> convertTimeWithTimezone(Collection<Task> tasks, @SessionAttribute User user) {
        for (Task task : tasks) {
            task.setCreated(task.getCreated()
                    .atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(ZoneId.of(user.getTimezone().getID())).toLocalDateTime());
        }
        return tasks;
    }
}
