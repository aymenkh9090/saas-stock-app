package com.aymen.saas.responses;

import com.aymen.saas.entities.TypeMvt;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockMvtResponse {


    private String id;
    private TypeMvt typeMvt;
    private Integer quantity;
    private LocalDate dateMvt;
    private String comment;



}
