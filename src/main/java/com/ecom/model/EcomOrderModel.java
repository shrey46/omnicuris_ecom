 package com.ecom.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * @author shrey
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

@Document(collection = "omnicomOrders")
public class EcomOrderModel {
    @Id
    private String id;

    private String userEmail;
    private String productId;

    private long orderedAt;
    private long deliverBy;
    private double orderTotal;
    private String paymentMode;

    private UserDetails userDetails;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class UserDetails {
        private String userId;
        private String userAddress;
        private String userContact;
    }

    private ProductDetails productDetails;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class ProductDetails{
        private String productName;
        private String productBrand;
        private String productCategory;
        private String productSubCategory;
        private int productQuantity;
        private double productPrice;
    }


}
