
package com.ysm.rprtrades.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductDTO {

    @NotBlank(message = "Product name is required.")
    @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters.")
    private String productName;

    @NotBlank(message = "Category is required.")
    @Size(max = 50, message = "Category cannot exceed 50 characters.")
    private String category;

    @NotBlank(message = "Description is required.")
    @Size(min = 5, max = 50, message = "Description must be between 10 and 1000 characters.")
    private String description;

    @NotNull(message = "Price is required.")
    @Positive(message = "Price must be greater than zero.")
    private Double price;

    @NotBlank(message = "Country of origin is required.")
    @Size(max = 100, message = "Country name cannot exceed 100 characters.")
    private String countryOfOrigin;

    @NotNull(message = "Stock quantity is required.")
    @PositiveOrZero(message = "Stock quantity cannot be negative.")
    private Integer stockQuantity;

    @NotNull(message = "Exporter is required.")
    private Long userId;

    // private String productName;
    // private String category;
    // private String description;
    // private Double price;
    // private String countryOfOrigin;
    // private Integer stockQuantity;
    // private Long userId;
}
