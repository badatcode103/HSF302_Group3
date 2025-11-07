package org.example.hsf302_group3.controller;

import jakarta.validation.Valid;
import org.example.hsf302_group3.entity.Department;
import org.example.hsf302_group3.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public String showDepartmentList(Model model) {
        model.addAttribute("departments", departmentService.findAll());
        return "department-list";
    }

    @GetMapping("/add")
    public String showAddDepartmentForm(Model model) {
        model.addAttribute("department", new Department());
        model.addAttribute("pageTitle", "Add New Department");
        return "department-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditDepartmentForm(@PathVariable("id") Integer id, Model model) {
        Department department = departmentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Invalid department Id:" + id));
        model.addAttribute("department", department);
        model.addAttribute("pageTitle", "Edit Department (ID: " + id + ")");
        return "department-form";
    }

    @PostMapping("/save")
    public String saveDepartment(@Valid @ModelAttribute("department") Department department,
                                 BindingResult bindingResult, Model model,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", department.getId() == null ? "Add New Department" : "Edit Department (ID: " + department.getId() + ")");
            return "department-form";
        }

        departmentService.save(department);
        redirectAttributes.addFlashAttribute("success", "Department saved successfully!");
        return "redirect:/departments";
    }

    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            departmentService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Department deleted successfully!");
        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("error", "Cannot delete department. It may still have students associated with it.");
        }
        return "redirect:/departments";
    }
}
