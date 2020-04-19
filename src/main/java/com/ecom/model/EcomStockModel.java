package com.ecom.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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


@Document(collection = "omnicomStocks")
public class EcomStockModel {

    //	public EcomModel() {
//	    updatedAt = createdAt = System.currentTimeMillis() / 1000;
//	  }
    @Id
    private String id;
    private String productName;
    private String productBrand;
    private String productCategory;
    private String productSubCategory;
    private double productPrice;
    private String productRatings;
    private int productStock;
    private String productSellerId;
    private long createdAt;
    private long updatedAt;

}
