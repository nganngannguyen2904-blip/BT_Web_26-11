package com.accounthub.controller;

import com.accounthub.entity.Category;
import com.accounthub.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String index() {
        return "redirect:/admin/categories/searchpaginated";
    }

    @RequestMapping("/searchpaginated")
    public String search(ModelMap model,
                         @RequestParam(name = "name", required = false) String name,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size) {
        int currentPage = Math.max(1, page.orElse(1));
        int pageSize = size.orElse(3);
        if (pageSize < 1 || pageSize > 100) {
            pageSize = 3;
        }

        String trimmedName = StringUtils.hasText(name) ? name.trim() : null;
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name"));
        Page<Category> resultPage = queryCategories(trimmedName, pageable);

        int totalPages = resultPage.getTotalPages();
        if (totalPages > 0 && currentPage > totalPages) {
            currentPage = totalPages;
            pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name"));
            resultPage = queryCategories(trimmedName, pageable);
        }

        if (trimmedName != null) {
            model.addAttribute("name", trimmedName);
        }

        if (totalPages > 0) {
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPages);
            if (totalPages > 5) {
                if (end == totalPages) {
                    start = Math.max(1, end - 4);
                } else if (start == 1) {
                    end = Math.min(totalPages, start + 4);
                }
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("categoryPage", resultPage);
        return "admin/category/list";
    }

    private Page<Category> queryCategories(String trimmedName, Pageable pageable) {
        if (trimmedName != null) {
            return categoryService.findByNameContaining(trimmedName, pageable);
        }
        return categoryService.findAll(pageable);
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category/add";
    }

    @PostMapping("/save")
    public String saveOrUpdate(@ModelAttribute("category") Category category,
                               RedirectAttributes redirectAttributes) {
        String trimmedName = category.getName() != null ? category.getName().trim() : "";
        if (!StringUtils.hasText(trimmedName)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tên danh mục không được để trống!");
            if (category.getId() != null) {
                return "redirect:/admin/categories/edit/" + category.getId();
            }
            return "redirect:/admin/categories/add";
        }

        boolean isNew = (category.getId() == null);
        category.setName(trimmedName);
        category.setImages(category.getImages() != null ? category.getImages().trim() : null);
        if (category.getStatus() == null) {
            category.setStatus(1);
        }

        categoryService.save(category);
        redirectAttributes.addFlashAttribute("message",
                isNew ? "Thêm danh mục mới thành công!" : "Cập nhật danh mục thành công!");
        return "redirect:/admin/categories/searchpaginated";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Category> opt = categoryService.findById(id);
        if (opt.isPresent()) {
            model.addAttribute("category", opt.get());
            return "admin/category/edit";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy danh mục có ID: " + id);
        return "redirect:/admin/categories/searchpaginated";
    }

    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        if (categoryService.findById(id).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy danh mục có ID: " + id);
            return "redirect:/admin/categories/searchpaginated";
        }
        try {
            categoryService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Xóa danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa danh mục này!");
        }
        return "redirect:/admin/categories/searchpaginated";
    }
}
