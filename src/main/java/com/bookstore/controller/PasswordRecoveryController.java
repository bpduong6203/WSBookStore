package com.bookstore.controller;

import com.bookstore.entity.User;
import com.bookstore.repository.IUserRepository;
import com.bookstore.services.EmailService;
import com.bookstore.services.UserServices;
import com.bookstore.services.VerificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordRecoveryController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private UserServices userServices;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgotpassword/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email) {
        String code = verificationService.generateVerificationCode(email);
        // Gửi email với mã xác nhận
        emailService.sendVerificationCode(email, code);
        return "redirect:/verify-code?email=" + email;
    }

    @GetMapping("/verify-code")
    public String showVerificationCodeForm(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "forgotpassword/verify-code"; // Trả về trang HTML cho người dùng nhập mã xác nhận
    }

    @PostMapping("/verify-code")
    public String handleVerificationCode(@RequestParam("email") String email, @RequestParam("code") String code, Model model) {
        boolean isValid = verificationService.verifyCode(email, code);
        if (isValid) {
            return "redirect:/reset-password?email=" + email;
        } else {
            model.addAttribute("error", "Mã xác nhận không đúng.");
            model.addAttribute("email", email);
            return "forgotpassword/verify-code";
        }
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "forgotpassword/reset-password";
    }

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam("email") String email,
                                      @RequestParam("password") String password,
                                      @RequestParam("confirmPassword") String confirmPassword,
                                      Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu và xác nhận mật khẩu không khớp.");
            model.addAttribute("email", email);
            return "forgotpassword/reset-password";
        }

        // Tìm kiếm người dùng bằng email
        User existingUser = userRepository.findByEmail(email);
        if (existingUser == null) {
            model.addAttribute("error", "Không tìm thấy người dùng với email này.");
            return "forgotpassword/reset-password";
        }

        // In ra thông tin người dùng để kiểm tra
        System.out.println("User ID: " + existingUser.getId());
        System.out.println("Old Password: " + existingUser.getPassword());

        // Mã hóa mật khẩu mới và cập nhật
        existingUser.setPassword(new BCryptPasswordEncoder().encode(password));
        userServices.save(existingUser);

        // In ra mật khẩu mới để kiểm tra
        System.out.println("New Password: " + existingUser.getPassword());

        return "redirect:/login";
    }

}
