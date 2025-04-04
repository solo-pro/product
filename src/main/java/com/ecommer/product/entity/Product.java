package com.ecommer.product.entity;


import com.ecommer.product.codegen.types.ProductUpdateInput;
import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;




@Table("PRODUCTS")
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
    private long categoryId;
    @Version
    @Column("PRODUCT_VERSION")
    private Long version;
    @Column("PRODUCT_DELETED")
    @Builder.Default
    private boolean deleted = false;
    @Column("PRODUCT_CREATED_TIME_STAMPS")
    @Builder.Default
    private long createdTimeStamps = System.currentTimeMillis();
    @Column("PRODUCT_UPDATED_TIME_STAMPS")
    @Builder.Default
    private long updatedTimeStamps = System.currentTimeMillis();

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", mainImage='" + mainImage + '\'' +
                ", description='" + description + '\'' +
                ", categoryId=" + categoryId +
                ", version=" + version +
                ", deleted=" + deleted +
                ", createdTimeStamps=" + createdTimeStamps +
                ", updatedTimeStamps=" + updatedTimeStamps +
                '}';
    }
    public void delete(){
        this.deleted = true;
    }
    public void update(ProductUpdateInput input){
        if(!StringUtil.isNullOrEmpty(input.getName())){
            this.name = input.getName();
        }
        if(input.getPrice() != null && input.getPrice() > 0){
            this.price = input.getPrice();
        }
        if(input.getStock() != null && input.getStock() > 0){
            this.stock = input.getStock();
        }
        if(!StringUtil.isNullOrEmpty(input.getMainImage())){
            this.mainImage = input.getMainImage();
        }
        if(!StringUtil.isNullOrEmpty(input.getDescription())){
            this.description = input.getDescription();
        }
        if(input.getCategoryId() != null && Long.parseLong(input.getCategoryId()) > 0){
            this.categoryId = Long.parseLong(input.getCategoryId());
        }
        this.updatedTimeStamps = System.currentTimeMillis();

    }
}
