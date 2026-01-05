package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import ru.job4j.service.TaskService;

@Controller
@AllArgsConstructor
public class IndexController {
    private final TaskService taskService;

    @GetMapping({"/", "/index"})
    public String getAll(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("filter", "all");
        return "index";
    }
}
