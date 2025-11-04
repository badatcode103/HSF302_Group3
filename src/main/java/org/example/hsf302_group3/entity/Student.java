package org.example.hsf302_group3.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Student ID is required")
    @Column(name = "studentid", nullable = false, unique = true, length = 20)
    private String studentid;

    @NotBlank(message = "Name is required")
    @Size(min = 5, max = 100, message = "Name must be between 5 and 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotNull(message = "GPA is required")
    @Min(value = 0, message = "GPA must be at least 0.0")
    @Max(value = 10, message = "GPA must be at most 10.0")
    @Column(nullable = false)
    private Double gpa;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    // Mối quan hệ Nhiều-1: Nhiều Student thuộc về 1 Department
    @NotNull(message = "Department is required")
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    @ToString.Exclude
    private Department department;
}
