package com.aymen.saas.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "products")
public class Product extends AbstractEntity{

    @Column(name = "name" , nullable = false)
    private String name;

    @Column(name = "reference" , nullable = false , unique = true)
    private String reference;

    @Column(name = "description" , columnDefinition = "TEXT")
    private String description;

    @Column(name = "alert_threshold" , nullable = false)
    private Integer alertThreshold;

    @Column(name = "price" , nullable = false)
    private BigDecimal price;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "product")
    private List<StockMvt> stockMvts;









}
