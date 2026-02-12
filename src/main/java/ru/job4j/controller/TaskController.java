package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.model.Task;
import ru.job4j.model.User;
import ru.job4j.service.TaskService;
import ru.job4j.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private TaskService taskService;
    private UserService userService;

    @GetMapping("/{id}")
    public String getViewPageById(Model model, @PathVariable int id) {
        var task = taskService.findById(id);
        model.addAttribute("task", task);
        return "tasks/view";
    }

    @GetMapping("/{id}/edit")
    public String getEditForm(@PathVariable int id, Model model) {
        var task = taskService.findById(id);
        model.addAttribute("task", task);
        return "tasks/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task) {
        taskService.update(task);
        return "redirect:/index";
    }

    @PostMapping("/{id}/complete")
    public String markAsDone(@PathVariable int id) {
        taskService.markAsDone(id);
        return "redirect:/index";
    }

    @GetMapping("/new")
    public String getCreationPage(Model model) {
        return "tasks/new";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, HttpSession httpSession) {
        User loggedUser = (User) httpSession.getAttribute("user");
        task.setUser(loggedUser);
        taskService.create(task);
        return "redirect:/index";
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
