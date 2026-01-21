package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.model.User;
import ru.job4j.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/register")
    public String getRegistrationPage() {
        return "users/register";
    }

    @PostMapping("/register")
    public String register(Model model, @ModelAttribute User user) {
        User savedUser = userService.save(user);
        if (savedUser == null) {
            model.addAttribute("message", "Пользователь с таким login уже существует");
            return "errors/404";
        }
        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model, HttpServletRequest request) {
        User foundUser = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
        if (foundUser == null) {
            model.addAttribute("error", "Почта или пароль введены неверно");
            return "users/login";
        }
        HttpSession session = request.getSession();
        session.setAttribute("user", foundUser);
        return "redirect:/index";
    }
}
