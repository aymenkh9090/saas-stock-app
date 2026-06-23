package com.aymen.saas.controllers;


import com.aymen.saas.repositories.CategoryRepository;
import com.aymen.saas.requests.CategoryRequest;
import com.aymen.saas.responses.CategoryResponse;
import com.aymen.saas.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Void> createCategory(
            @Valid
            @RequestBody final CategoryRequest request
            ){
            this.categoryService.create(request);
            return ResponseEntity.ok().build();
    }

    @PutMapping("/{category-id}")
    public ResponseEntity<Void> updateCategory(
            @Valid
            @RequestBody final CategoryRequest request,
            @PathVariable("category-id") String id
    ){
        this.categoryService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{category-id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @PathVariable("category-id") String id
    ){
        return ResponseEntity.ok(this.categoryService.findById(id));
    }

    @DeleteMapping("/{category-id}")
    public  ResponseEntity<Void> deleteCtegory(
            @PathVariable("category-id") String id
    ){
        this.categoryService.delete(id);
        return ResponseEntity.ok().build();
    }



}
