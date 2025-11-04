package org.example.hsf302_group3.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.hsf302_group3.entity.Student;
import org.example.hsf302_group3.entity.UserAccount;
import org.example.hsf302_group3.service.DepartmentService;
import org.example.hsf302_group3.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public String showStudentList(Model model, HttpSession session,
                                  @RequestParam(defaultValue = "0") int page) {

        UserAccount user = (UserAccount) session.getAttribute("user");
        model.addAttribute("user", user); // Gửi thông tin user ra view

        if (user.getRole() == 1) {
            // ----- LOGIC CHO MANAGER -----
            model.addAttribute("students", studentService.getStudentsForManager());
            model.addAttribute("isManagerView", true);
        } else {
            // ----- LOGIC CHO STAFF -----
            Pageable pageable = PageRequest.of(page, 5); // 5 sinh viên mỗi trang
            Page<Student> studentPage = studentService.getStudentsForStaff(pageable);

            model.addAttribute("studentPage", studentPage);
            model.addAttribute("isManagerView", false);
        }

        return "student-list"; // Trả về file student-list.html
    }

    @GetMapping("/add")
    public String showAddStudentForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("pageTitle", "Add New Student");
        return "student-form"; // Trả về file student-form.html
    }

    @GetMapping("/edit/{id}")
    public String showEditStudentForm(@PathVariable("id") Integer id, Model model,
                                      HttpSession session, RedirectAttributes redirectAttributes) {

        UserAccount user = (UserAccount) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Please login to continue.");
            return "redirect:/";
        }

        Student student = studentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Invalid student Id:" + id));

        // Kiểm tra quyền sở hữu khi EDIT (Requirement)
        if (user.getRole() == 2 && (student.getCreatedBy() == null || !student.getCreatedBy().equals(user.getUsername()))) {
            redirectAttributes.addFlashAttribute("error", "You can only edit students you created.");
            return "redirect:/students";
        }

        model.addAttribute("student", student);
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("pageTitle", "Edit Student (ID: " + id + ")");
        return "student-form";
    }

    @PostMapping("/save")
    public String saveStudent(@Valid @ModelAttribute("student") Student student,
                              BindingResult bindingResult, Model model,
                              HttpSession session, RedirectAttributes redirectAttributes) {

        // 1. Kiểm tra Validation
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.findAll());
            // Giữ nguyên pageTitle
            model.addAttribute("pageTitle", student.getId() == null ? "Add New Student" : "Edit Student (ID: " + student.getId() + ")");
            return "student-form"; // Quay lại form nếu lỗi
        }

        // 2. Lấy username từ session
        UserAccount user = (UserAccount) session.getAttribute("user");

        try {
            // 3. Gọi Service để lưu
            studentService.saveStudent(student, user.getUsername());
            redirectAttributes.addFlashAttribute("success", "Student saved successfully!");
        } catch (SecurityException e) {
            // 4. Bắt lỗi nếu cố gắng sửa của người khác
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred: " + e.getMessage());
        }

        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Integer id,
                                HttpSession session, RedirectAttributes redirectAttributes) {

        UserAccount user = (UserAccount) session.getAttribute("user");

        try {
            studentService.deleteStudent(id, user.getUsername());
            redirectAttributes.addFlashAttribute("success", "Student deleted successfully!");
        } catch (SecurityException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred: " + e.getMessage());
        }

        return "redirect:/students";
    }
}
