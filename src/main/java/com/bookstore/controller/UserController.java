package com.bookstore.controller;

import com.bookstore.entity.User;
import com.bookstore.repository.IUserRepository;
import com.bookstore.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserServices userServices;

    IUserRepository userRepository;

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, Model model) {
        model.addAttribute("error", error);
        if (logout != null) {
            model.addAttribute("message", "Bạn đã đăng xuất");
        }
        model.addAttribute("user", new User());
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model model) {
        if (user.getUsername() == null || user.getPassword() == null) {
            model.addAttribute("error", "Tên đăng nhập và mật khẩu không được để trống");
            return "user/login";
        }
        if (!userServices.checkLogin(user.getUsername(), user.getPassword())) {
            model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu");
            return "user/login";
        }
        return "redirect:/home"; // Đăng nhập thành công, chuyển hướng về trang chủ
    }

    @GetMapping("/check-username")
    @ResponseBody
    public boolean checkUsername(@RequestParam String username) {
        List<String> existingUsernames = userRepository.findAllUsernames();
        return existingUsernames.contains(username);
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                model.addAttribute(error.getField() + "_error", error.getDefaultMessage());
            }
            return "user/login";
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.user", "Mật khẩu và xác nhận mật khẩu không khớp");
            return "user/login";
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userServices.save(user);

        return "redirect:/login";
    }
}
