package com.example.date_values.entity;

import javax.persistence.criteria.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Getter
@Setter
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public class BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "created_date")
    private Long createDate;

    @Column(name = "updated_date")
    private Long updateDate;

//    @Column(name = "created_by")
//    @CreatedBy
//    private String createdBy;
//
//    @Column(name = "updated_by")
//    @LastModifiedBy
//    private String updatedBy;

}
