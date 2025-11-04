package org.example.hsf302_group3.service;

import org.example.hsf302_group3.entity.Department;
import org.example.hsf302_group3.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Optional<Department> findById(Integer id) {
        return departmentRepository.findById(id);
    }

    public void save(Department department) {
        departmentRepository.save(department);
    }

    public void deleteById(Integer id) {
        // Cần kiểm tra xem có sinh viên nào thuộc khoa này không trước khi xóa
        // (Trong phạm vi bài này, ta tạm bỏ qua để đơn giản hóa)
        departmentRepository.deleteById(id);
    }
}