package com.aymen.saas.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "stck-mvts")
public class StockMvt extends AbstractEntity {


    @Column(name = "type-mvt",nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeMvt typeMvt;

    @Column(name = "quantity" , nullable = false)
    private Integer quantity;

    @Column(name = "date-mvt",nullable = false)
    private LocalDate dateMvt;

    @Column(name = "comment",nullable = false,columnDefinition = "TEXT")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;





}
