package com.aymen.saas.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
//@FilterDef(   we just use the filter in the first approche " schared database"
//        name = "tenantFilter",
//        parameters = @ParamDef (name = "tenantId" , type = String.class),
//        defaultCondition = "tenant_id = :tenantId"
//)
//@Filter(name = "tenantFilter")
public class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id" , nullable = false , updatable = false)
    private String id;

//    @Column(name = "tenant_id" , nullable = false )
//    private String tenantId;

    @CreatedDate
    @Column(name = "created_at" , nullable = false , updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at" , insertable = false)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by" ,  nullable = false , updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by" , insertable = false)
    private String updatedBy;

    @Column(name = "deleted" , nullable = false)
    private Boolean deleted;

    @PrePersist
    protected void onCreate(){
        if(this.deleted == null){
            this.deleted = false;
        }
//        if(this.tenantId == null){
//            this.tenantId = "default";
//        }
    }






}
