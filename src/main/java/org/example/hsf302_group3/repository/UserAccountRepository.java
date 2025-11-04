package org.example.hsf302_group3.repository;

import org.example.hsf302_group3.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {

    // Tự động sinh câu lệnh WHERE username = ?
    Optional<UserAccount> findByUsername(String username);
}