package com.ecom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shrey
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EcomStockDto {

    @JsonProperty(required = false)
    private String productName;
    @JsonProperty(required = false)
    private String productBrand;
    @JsonProperty(required = false)
    private String productCategory;
    @JsonProperty(required = false)
    private String productSubCategory;
    @JsonProperty(required = false)
    private double productPrice;
    @JsonProperty(required = false)
    private String productRatings;
    @JsonProperty(required = false)
    private int productStock;
    @JsonProperty(required = false)
    private String productSellerId;

}
