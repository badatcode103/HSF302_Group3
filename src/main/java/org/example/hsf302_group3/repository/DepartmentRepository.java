package org.example.hsf302_group3.repository;

import org.example.hsf302_group3.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    // Các hàm CRUD cơ bản (findAll, findById, save, delete) đã có sẵn
}