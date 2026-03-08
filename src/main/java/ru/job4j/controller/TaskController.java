package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.model.Category;
import ru.job4j.model.Task;
import ru.job4j.model.User;
import ru.job4j.service.CategoryService;
import ru.job4j.service.PriorityService;
import ru.job4j.service.TaskService;
import ru.job4j.service.UserService;
import ru.job4j.utility.TimeUtility;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private TaskService taskService;
    private PriorityService priorityService;
    private CategoryService categoryService;

    @GetMapping("/{id}")
    public String getViewPageById(Model model, @PathVariable int id, HttpSession session) {
        var task = taskService.findById(id);
        var loggedUser = (User) session.getAttribute("user");
        model.addAttribute("task", TimeUtility.convertTaskToUserTimeZone(task, loggedUser.getTimezone()));
        return "tasks/view";
    }

    @GetMapping("/{id}/edit")
    public String getEditForm(@PathVariable int id, Model model, HttpSession session) {
        var task = taskService.findById(id);
        var loggedUser = (User) session.getAttribute("user");
        model.addAttribute("task", TimeUtility.convertTaskToUserTimeZone(task, loggedUser.getTimezone()));
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
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "tasks/new";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task,
                         @RequestParam (name = "categoryIds") List<Integer> categoriesList,
                         HttpSession session) {
        var loggedUser = (User) session.getAttribute("user");
        task.setUser(loggedUser);
        List<Category> selectedCategory = categoryService.findAllById(categoriesList);
        task.setCategories(selectedCategory);
        taskService.create(task);
        return "redirect:/index";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id) {
        taskService.delete(id);
        return "redirect:/index";
    }

    @GetMapping("/completed")
    public String getCompleted(Model model, HttpSession session) {
        var loggedUser = (User) session.getAttribute("user");
        var tasks = taskService.getCompleted();
        var tasksWithUserZones = TimeUtility.convertTasksToUserTimeZone(tasks, loggedUser.getTimezone());
        model.addAttribute("tasks", tasksWithUserZones);
        model.addAttribute("filter", "completed");
        return "index";
    }

    @GetMapping("/new-tasks")
    public String getNew(Model model, HttpSession session) {
        var loggedUser = (User) session.getAttribute("user");
        var tasks = taskService.getNew();
        var tasksWithUserZones = TimeUtility.convertTasksToUserTimeZone(tasks, loggedUser.getTimezone());
        model.addAttribute("tasks", tasksWithUserZones);
        model.addAttribute("filter", "new");
        return "index";
    }
}
