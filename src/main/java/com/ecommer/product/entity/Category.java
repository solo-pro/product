package com.ecommer.product.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "CATEGORIES")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @Column("CATEGORY_ID")
    private Long id;
    @Column("CATEGORY_NAME")
    private String name;
    @Column("CATEGORY_DESCRIPTION")
    @Builder.Default
    private String description = "";
    @Column("CATEGORY_CREATED_TIME_STAMPS")
    @Builder.Default
    private Long createdTimeStamps = System.currentTimeMillis();
    @Column("CATEGORY_UPDATED_TIME_STAMPS")
    @Builder.Default
    private Long updatedTimeStamps = System.currentTimeMillis();
}
