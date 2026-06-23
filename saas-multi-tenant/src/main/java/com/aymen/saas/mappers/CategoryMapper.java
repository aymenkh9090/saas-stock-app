package com.aymen.saas.mappers;

import com.aymen.saas.entities.Category;
import com.aymen.saas.requests.CategoryRequest;
import com.aymen.saas.responses.CategoryResponse;
import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {

    public Category toEntity(final CategoryRequest request) {

        return Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .deleted(false)
                .build();
    }


    public CategoryResponse toResponse(final Category entity){
        return CategoryResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }








}
