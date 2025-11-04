package org.example.hsf302_group3.repository;

import org.example.hsf302_group3.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    // Hàm này rất quan trọng
    // Tự động sinh câu lệnh: SELECT TOP 5 * FROM students ORDER BY gpa DESC
    // Dùng cho vai trò Manager
    List<Student> findTop5ByOrderByGpaDesc();

    // JpaRepository đã có sẵn hàm findAll(Pageable pageable) cho Staff
}
