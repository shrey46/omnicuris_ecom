package com.ecom.service;

import com.ecom.dto.EcomOrderDto;
import com.ecom.dto.EcomStockDto;
import com.ecom.dto.EcomUserDto;
import com.ecom.model.EcomOrderModel;
import com.ecom.model.EcomStockModel;

import java.util.List;

/**
 * @author shrey
 */

public interface EcomService {

    public List<EcomStockModel> searchProducts(String search);

    public List<EcomOrderModel> searchOrders(String search);

    public boolean validateOrderDto(EcomOrderDto ecomOrderDto);

    public void placeOrders(EcomOrderDto ecomOrderDto);

    public void userValidations(EcomUserDto ecomUserDto);

    public void updateProducts(String id, EcomStockDto ecomStockDto);
}
