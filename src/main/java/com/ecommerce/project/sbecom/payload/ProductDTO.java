package com.ecommerce.project.sbecom.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;

    @NotBlank
    @Size(min = 1, max = 100)
    private String productName;
    private String image;
    private String description;
    private Integer quantity;
    private double price;
    private double discount;
    private Double specialPrice;

}

