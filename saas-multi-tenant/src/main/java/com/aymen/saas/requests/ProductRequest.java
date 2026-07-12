package com.aymen.saas.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Product name does not be empty")
    @Size(min = 3,max = 255,message = "Product name does be between 3 and 255")
    private String name;

    @NotBlank(message = "Product reference does not be empty")
    @Size(min = 3,max = 255,message = "Product reference does be between 3 and 255")
    private String reference;


    @Size(min = 3,max = 255,message = "Product description does be between 3 and 255")
    private String description;

    @Positive(message = "alertThreshold does not be a negative")
    private Integer alertThreshold;

    @Positive(message = "Price does not be a negative")
    private BigDecimal price;

    @NotBlank(message = "CategoryId does not be empty")
    private String categoryId;






}
