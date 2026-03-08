package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import ru.job4j.model.Task;
import ru.job4j.model.User;
import ru.job4j.service.TaskService;
import ru.job4j.utility.TimeUtility;

import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
public class IndexController {
    private final TaskService taskService;

    @GetMapping({"/", "/index"})
    public String getAll(Model model, HttpSession session) {
        var tasks = taskService.findAllWithDetails();
        User currentUser = (User) session.getAttribute("user");
        var tasksWithUseZones = TimeUtility.convertTasksToUserTimeZone(tasks, currentUser.getTimezone());
        model.addAttribute("tasks", tasksWithUseZones);
        model.addAttribute("filter", "all");
        return "index";
    }
}
