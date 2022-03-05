package com.example.crawlingdata.apis;

import java.util.List;

import com.example.crawlingdata.repositories.CategoryRepository;
import com.example.crawlingdata.responses.models.Category;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/categories")
public class CategoryApiController {
    
    private final CategoryRepository cateRepo;
    public CategoryApiController(
        CategoryRepository cateRepo
    ) {
        this.cateRepo = cateRepo;
    }

    @GetMapping(value = {"", "/"})
    public List<Category> getCategories() {
        return cateRepo.findAll();
    }
}
