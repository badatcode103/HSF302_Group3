package org.example.hsf302_group3.service;

import org.example.hsf302_group3.entity.Student;
import org.example.hsf302_group3.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // Dành cho Staff (có phân trang)
    public Page<Student> getStudentsForStaff(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    // Dành cho Manager (Top 5)
    public List<Student> getStudentsForManager() {
        return studentRepository.findTop5ByOrderByGpaDesc();
    }

    public Optional<Student> findById(Integer id) {
        return studentRepository.findById(id);
    }

    /**
     * Xử lý logic lưu (Thêm mới hoặc Cập nhật) sinh viên
     * @param student Đối tượng sinh viên từ form
     * @param username Tên của Staff đang đăng nhập
     */
    public void saveStudent(Student student, String username) {
        if (student.getId() == null) {
            // ----- LOGIC THÊM MỚI -----
            student.setCreatedAt(LocalDate.now());
            student.setUpdatedAt(LocalDate.now());
            student.setCreatedBy(username);
            studentRepository.save(student);
        } else {
            // ----- LOGIC CẬP NHẬT -----
            // 1. Lấy bản ghi gốc từ CSDL
            Optional<Student> optionalExisting = studentRepository.findById(student.getId());
            if (optionalExisting.isEmpty()) {
                throw new RuntimeException("Student not found!");
            }
            Student existingStudent = optionalExisting.get();

            // 2. Kiểm tra quyền sở hữu (Requirement)
            if (!existingStudent.getCreatedBy().equals(username)) {
                throw new SecurityException("You do not have permission to update this student.");
            }

            // 3. Cập nhật các trường từ form vào bản ghi gốc
            existingStudent.setStudentid(student.getStudentid());
            existingStudent.setName(student.getName());
            existingStudent.setGpa(student.getGpa());
            existingStudent.setDepartment(student.getDepartment());

            // 4. Set ngày cập nhật (Requirement)
            existingStudent.setUpdatedAt(LocalDate.now());

            // 5. Lưu bản ghi đã cập nhật (bản ghi gốc)
            // (Tuyệt đối không lưu 'student' từ form vì nó thiếu 'createdAt' và 'createdBy')
            studentRepository.save(existingStudent);
        }
    }

    /**
     * Xử lý logic xóa sinh viên
     * @param id ID sinh viên cần xóa
     * @param username Tên của Staff đang đăng nhập
     */
    public void deleteStudent(Integer id, String username) {
        // 1. Lấy bản ghi gốc từ CSDL
        Optional<Student> optionalExisting = studentRepository.findById(id);
        if (optionalExisting.isEmpty()) {
            throw new RuntimeException("Student not found!");
        }
        Student existingStudent = optionalExisting.get();

        // 2. Kiểm tra quyền sở hữu (Requirement)
        if (!existingStudent.getCreatedBy().equals(username)) {
            throw new SecurityException("You do not have permission to delete this student.");
        }

        // 3. Xóa nếu có quyền
        studentRepository.delete(existingStudent);
    }
}