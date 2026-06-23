package com.aymen.saas.services.impl;

import com.aymen.saas.entities.Category;
import com.aymen.saas.mappers.CategoryMapper;
import com.aymen.saas.repositories.CategoryRepository;
import com.aymen.saas.requests.CategoryRequest;
import com.aymen.saas.responses.CategoryResponse;
import com.aymen.saas.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public void create(CategoryRequest request) {

        // Check if category exist By Name
        checkIfCategoryExistByName(request.getName());
        final Category category = categoryMapper.toEntity(request);
        this.categoryRepository.save(category);
    }


    @Override
    public void update(String id, CategoryRequest request) {

        final Optional<Category> existingCategory = this.categoryRepository.findById(id);
        if(existingCategory.isEmpty()){
            log.error("Category not found with id={}", id);
            throw new EntityNotFoundException("Category not found");
        }

        final Category category = existingCategory.get();
        // check if category already  exist
        if(!category.getName().equalsIgnoreCase(request.getName())){
            checkIfCategoryExistByName(request.getName());
        }
        final Category updatedCategory = categoryMapper.toEntity(request);
        updatedCategory.setId(id);
        this.categoryRepository.save(updatedCategory);
    }

    @Override
    public CategoryResponse findById(String id) {
        return this.categoryRepository.findById(id)
                .map(this.categoryMapper::toResponse)
                .orElseThrow(()-> new EntityNotFoundException("Category not found"));
    }

    @Override
    public void delete(String id) {
        final Category category = this.categoryRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Category not found"));
        this.categoryRepository.delete(category);
    }

    private void checkIfCategoryExistByName(String name) {

        final Optional<Category> category =
                this.categoryRepository.findByNameIgnoreCase(name);
        if(category.isPresent()) {
            log.debug("Category with name {} already exists", name);
            throw  new RuntimeException("Category already exists");
        }

    }





}
