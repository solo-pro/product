package com.ecommer.product.entity;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;



@Table(name = "PRODUCTS")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @Column("PRODUCT_ID")
    private Long id;
    @Column("PRODUCT_NAME")
    private String name;
    @Column("PRODUCT_PRICE")
    private long price;
    @Column("PRODUCT_STOCK")
    private long stock;
    @Column("PRODUCT_MAIN_IMAGE")
    private String mainImage;
    @Column("PRODUCT_DESCRIPTION")
    private String description;
    @Column("PRODUCT_CATEGORY_ID")
    private Long categoryId;
    @Version
    @Column("PRODUCT_VERSION")
    private Long version;
    @Column("PRODUCT_DELETED")
    @Builder.Default
    private boolean deleted = false;
    @Column("PRODUCT_CREATED_TIME_STAMPS")
    @Builder.Default
    private Long createdTimeStamps = System.currentTimeMillis();
    @Column("PRODUCT_UPDATED_TIME_STAMPS")
    @Builder.Default
    private Long updatedTimeStamps = System.currentTimeMillis();
}
