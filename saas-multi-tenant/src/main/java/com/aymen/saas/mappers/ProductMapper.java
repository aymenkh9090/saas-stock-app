package com.aymen.saas.mappers;


import com.aymen.saas.entities.Category;
import com.aymen.saas.entities.Product;
import com.aymen.saas.requests.ProductRequest;
import com.aymen.saas.responses.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {


    //  Dto====>Entity : productRequest===>product
    public Product toEntiy( final ProductRequest request){

        return Product.builder()
                .name( request.getName() )
                .reference( request.getReference() )
                .description( request.getDescription() )
                .alertThreshold( request.getAlertThreshold() )
                .price( request.getPrice() )
                .category(
                        Category.builder()
                                .id(request.getCategoryId())
                                .build()
                )
                .deleted(false)
                .build();
    }



    // entity==>Dto : product===>productResponse
    public ProductResponse toResponse(final Product product){

        return  ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .reference(product.getReference())
                .description(product.getDescription())
                .alertThreshold(product.getAlertThreshold())
                .price(product.getPrice())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .build();

    }









}
