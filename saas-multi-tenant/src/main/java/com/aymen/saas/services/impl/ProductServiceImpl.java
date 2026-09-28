package com.aymen.saas.services.impl;

import com.aymen.saas.common.PageResponse;
import com.aymen.saas.entities.Category;
import com.aymen.saas.entities.Product;
import com.aymen.saas.mappers.ProductMapper;
import com.aymen.saas.repositories.CategoryRepository;
import com.aymen.saas.repositories.ProductRepository;
import com.aymen.saas.requests.ProductRequest;
import com.aymen.saas.responses.ProductResponse;
import com.aymen.saas.services.ProductService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public void create(final ProductRequest request) {
        // check if product exist by referance
        checkIfProductAlreadyExistsByReference(request.getReference());

        // check if category exists
        checkIfCategoryExistById(request.getCategoryId());

        final Product entity = productMapper.toEntiy(request);
        productRepository.save(entity);
    }



    @Override
    public void update(String id, ProductRequest request) {

        // check if product exists
        final Optional<Product> productExists =
                this.productRepository.findById(id);
        if(productExists.isEmpty()){
            log.debug("Product does not exist");
            throw new RuntimeException("Product does not exiist");
        }

        // check if produt already exists
        if(!productExists.get().getReference().equalsIgnoreCase(request.getReference())){
            checkIfProductAlreadyExistsByReference(request.getReference());
        }

        // check if category exists
        checkIfCategoryExistById(request.getCategoryId());

        final Product productToUpdate = this.productMapper.toEntiy(request);
        productToUpdate.setId(id);
        this.productRepository.save(productToUpdate);

    }

    @Override
    public ProductResponse findById(String id) {
        return this.productRepository.findById(id)
                .map(this.productMapper::toResponse)
                .orElseThrow(()-> new RuntimeException("Product does not exist"));

    }

    @Override
    public List<ProductResponse> findAll() {
        return List.of();
    }

    @Override
    public PageResponse<ProductResponse> findAll(final int page , final int size) {
        final PageRequest pageRequest = PageRequest.of(page , size);
        final Page<Product> productPage = productRepository.findAll(pageRequest);
        final Page<ProductResponse> productResponses = productPage.map(productMapper::toResponse);
        return PageResponse.of(productResponses);
    }

    @Override
    public void delete(final String id) {
        final Product product = productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Product does not exist"));
        productRepository.delete(product);
    }



    private void checkIfProductAlreadyExistsByReference(String reference) {

        final Optional<Product> product = productRepository.findByReferenceIgnoreCase(reference);
        if(product.isPresent()) {
            log.debug("Product already exists with reference {}", reference);
            // after that we gonna make a special exceptions "Duplicate Product"
            throw new RuntimeException("Product already exists with reference " + reference);
        }

    }


    private void checkIfCategoryExistById(String categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isEmpty()) {
            log.debug("Category not found with id {}", categoryId);
            // later will change by EntityNotFoundException
            throw new RuntimeException("Category not found with id " + categoryId);
        }
    }





}
