package org.example.hsf302_group3.service;

import org.example.hsf302_group3.entity.UserAccount;
import org.example.hsf302_group3.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    /**
     * Kiểm tra thông tin đăng nhập.
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return UserAccount nếu hợp lệ, null nếu thất bại
     */
    public UserAccount login(String username, String password) {
        Optional<UserAccount> optionalUser = userAccountRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            UserAccount user = optionalUser.get();
            // Trong dự án này, ta so sánh mật khẩu trực tiếp (theo data.sql)
            // Trong thực tế, bạn phải dùng BCryptPasswordEncoder
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null; // Đăng nhập thất bại
    }
}
