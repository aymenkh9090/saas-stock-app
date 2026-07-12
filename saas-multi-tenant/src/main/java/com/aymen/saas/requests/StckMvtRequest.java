package com.aymen.saas.requests;

import com.aymen.saas.entities.TypeMvt;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StckMvtRequest {


    private TypeMvt typeMvt;

    @NotBlank(message = "Quantity does not be empty")
    @Positive(message = "Quantity must be a positive")
    private Integer quantity;

    @NotBlank(message = "Date of Mouvement not be empty")
    @PastOrPresent(message = "Date of mouvement must be in past or present")
    private LocalDate dateMvt;

    private String comment;

    @NotBlank(message = "ProductId does not be empty")
    private String productId;






}
