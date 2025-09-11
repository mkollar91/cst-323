package edu.gcu.cst323.cloud_test_app;

import java.time.Instant;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    private final UserRepo repo;

    public UserController(UserRepo repo) {
        this.repo = repo;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/users")
    public String list(Model model) {
        model.addAttribute("users", repo.findAll());
        return "users";
    }

    @GetMapping("/users/new")
    public String form(Model model) {
        model.addAttribute("user", new User());
        return "form";
    }

    @PostMapping("/users")
    public String create(@ModelAttribute User u) {
        repo.save(u);
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/delete")
    public String delete(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        User user = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "edit";
    }

    @PostMapping("/users/{id}/edit")
    public String update(@PathVariable Long id, @ModelAttribute User user) {
        User existing = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setEmail(user.getEmail());
        existing.setUpdatedAt(Instant.now()); // <-- track last modified
        repo.save(existing);
        return "redirect:/users";
    }

}
