package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.model.Task;
import ru.job4j.service.TaskService;

@Controller
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private TaskService taskService;

    @GetMapping("/{id}")
    public String getViewPageById(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задача с ID " + id + " не найдена");
            return "error/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/view";
    }

    @GetMapping("/{id}/edit")
    public String getEditForm(@PathVariable int id, Model model) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задача с ID " + id + " не найдена");
            return "error/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, Model model) {
        try {
            boolean isUpdated = taskService.update(task);
            if (!isUpdated) {
                model.addAttribute("message", "Обновление не выполнено");
                return "errors/409";
            }
            return "redirect:/index";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/new")
    public String getCreationPage(Model model) {
        return "tasks/new";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, Model model) {
        try {
            taskService.create(task);
            return "redirect:/index";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/404";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id) {
        taskService.delete(id);
        return "redirect:/index";
    }

    @GetMapping("/completed")
    public String getCompleted(Model model) {
        model.addAttribute("tasks", taskService.getCompleted());
        model.addAttribute("filter", "completed");
        return "index";
    }

    @GetMapping("/new-tasks")
    public String getNew(Model model) {
        model.addAttribute("tasks", taskService.getNew());
        model.addAttribute("filter", "new");
        return "index";
    }
}
