package org.example.hsf302_group3.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.hsf302_group3.entity.UserAccount;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        UserAccount user = (UserAccount) session.getAttribute("user");

        // 1. Kiểm tra đã đăng nhập chưa
        if (user == null) {
            response.sendRedirect("/"); // Chuyển về trang login
            return false; // Dừng xử lý
        }

        String requestURI = request.getRequestURI();
        int role = user.getRole();

        // 2. Kiểm tra quyền Guest (Role 3)
        if (role == 3) {
            session.setAttribute("error", "You have no permission (Guest).");
            session.removeAttribute("user"); // Đăng xuất Guest
            response.sendRedirect("/");
            return false;
        }

        // 3. Kiểm tra quyền Staff (Role 2)
        // Staff chỉ được vào /students và các đường dẫn con, không được vào /departments
        if (role == 2 && requestURI.startsWith("/departments")) {
            session.setAttribute("error", "You do not have permission for Department Management.");
            response.sendRedirect("/students"); // Chuyển về trang chính của Staff
            return false;
        }

        // 4. Manager (Role 1) có mọi quyền
        // (Không cần check gì thêm)

        return true; // Cho phép tiếp tục
    }
}
