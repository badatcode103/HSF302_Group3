package org.example.hsf302_group3.controller;

import jakarta.servlet.http.HttpSession;
import org.example.hsf302_group3.entity.UserAccount;
import org.example.hsf302_group3.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/")
    public String showLoginPage(Model model, HttpSession session) {
        // Lấy lỗi (nếu có) từ Interceptor hoặc từ lần đăng nhập trước
        String error = (String) session.getAttribute("error");
        if (error != null) {
            model.addAttribute("error", error);
            session.removeAttribute("error"); // Xóa lỗi sau khi hiển thị
        }
        return "login"; // Trả về file login.html
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        UserAccount user = userAccountService.login(username, password);

        if (user != null) {
            session.setAttribute("user", user); // Lưu thông tin user vào session
            return "redirect:/students"; // Đăng nhập thành công, chuyển đến trang student
        } else {
            // Đăng nhập thất bại
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/"; // Quay lại trang login
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("user"); // Xóa session
        session.invalidate();
        return "redirect:/"; // Về trang login
    }
}