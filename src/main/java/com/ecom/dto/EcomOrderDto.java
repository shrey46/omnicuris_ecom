package com.ecom.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shrey
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

public class EcomOrderDto {

    private String userEmail;
    private String productId;
    private int productQuantity;
    private String paymentMode;
}
