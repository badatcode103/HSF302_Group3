package org.example.hsf302_group3.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Department name is required")
    @Size(min = 5, max = 100, message = "Department name must be between 5 and 100 characters")
    @Column(name = "departmentname", nullable = false, length = 100)
    private String departmentname;

    // Thể hiện mối quan hệ 1-Nhiều: Một Department có nhiều Student
    @OneToMany(mappedBy = "department")
    private List<Student> students;
}