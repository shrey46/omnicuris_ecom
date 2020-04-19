package com.ecom.controller;

import java.util.Date;
import java.util.List;

import com.ecom.constant.EcomPromptMessage;
import com.ecom.dto.EcomOrderDto;
import com.ecom.dto.EcomStockDto;
import com.ecom.dto.EcomUserDto;
import com.ecom.model.EcomOrderModel;
import com.ecom.model.EcomUserModel;
import com.ecom.repository.EcomOrderRepository;
import com.ecom.repository.EcomUserRepository;
import com.ecom.service.EcomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import com.ecom.beans.EcomApplicationResponse;
import com.ecom.exception.GenericException;
import com.ecom.model.EcomStockModel;
import com.ecom.repository.EcomStockRepository;

import javax.validation.Valid;

/**
 * @author shrey
 */
@RestController
@CrossOrigin
@RequestMapping("/omnicom")
public class EcomController {

    @Autowired
    private EcomStockRepository repository;

    @Autowired
    private EcomUserRepository userRepository;

    @Autowired
    private EcomOrderRepository orderRepository;

    @Autowired
    private MongoTemplate mongoOperations;

    @Autowired
    private EcomService ecomService;


    //ADD USERS FOR ORDERING
    @PostMapping("/addUsers")
    public EcomApplicationResponse saveUser(@RequestBody List<EcomUserDto> ecomUserDtos) {
        for(EcomUserDto ecomUserDto:ecomUserDtos)
            ecomService.userValidations(ecomUserDto);
        return EcomApplicationResponse.success(EcomPromptMessage.USER_ADDED_SUCCESSFULLY);
    }


    //SELLER api's
    @PostMapping("/addProduct")
    public EcomApplicationResponse saveProduct(@RequestBody EcomStockModel ecomModel) {

        long time = System.currentTimeMillis();
        ecomModel.setCreatedAt(time);
        ecomModel.setUpdatedAt(time);
        Date date = new java.util.Date(time);
        repository.save(ecomModel);
        return EcomApplicationResponse.success(EcomPromptMessage.PRODUCT_ADDED_SUCCESSFULLY
                + EcomPromptMessage.WITH_ID + ecomModel.getId() + EcomPromptMessage.AT_DELIMITER + date);
    }
    @PostMapping("/addProducts")
    public EcomApplicationResponse saveProduct(@RequestBody List<EcomStockModel> ecomModels) {
        for(EcomStockModel ecomModel:ecomModels)
        {
            long time = System.currentTimeMillis();
            ecomModel.setCreatedAt(time);
            ecomModel.setUpdatedAt(time);
            Date date = new java.util.Date(time);
            repository.save(ecomModel);
        }
        return EcomApplicationResponse.success(EcomPromptMessage.PRODUCT_ADDED_SUCCESSFULLY);
    }
    @GetMapping("/findAllProducts")
    public EcomApplicationResponse getAllProducts() {

        return EcomApplicationResponse.success(repository.findAll());
    }


    @GetMapping("/findProduct/{search}")
    public EcomApplicationResponse getProduct(@PathVariable String search) {

        return EcomApplicationResponse.success(ecomService.searchProducts(search));
    }

    @DeleteMapping("/deleteProduct/{id}")
    public EcomApplicationResponse deleteProduct(@PathVariable String id) {
        EcomStockModel user = repository.findById(id)
                .orElseThrow(() -> new GenericException(EcomPromptMessage.INVALID_PRODUCT_ID));
        repository.deleteById(id);
        Date date = new java.util.Date(System.currentTimeMillis());
        return EcomApplicationResponse.success(EcomPromptMessage.PRODUCT_DELETED_SUCCESSFULLY
                + EcomPromptMessage.WITH_ID + id + EcomPromptMessage.AT_DELIMITER + date);

    }

    @PutMapping("/updateProduct/{id}")
    public EcomApplicationResponse updateProduct(@PathVariable("id") String id, @Valid @RequestBody EcomStockDto stockDto) {

        ecomService.updateProducts(id, stockDto);
        return EcomApplicationResponse.success(EcomPromptMessage.PRODUCT_UPDATED_SUCCESSFULLY
                + EcomPromptMessage.WITH_ID + id);
    }


    //order api

    //single product
    @PostMapping("/addOrder")
    public EcomApplicationResponse saveOrder(@RequestBody EcomOrderDto ecomOrderDto) {
        if (ecomService.validateOrderDto(ecomOrderDto))
            ecomService.placeOrders(ecomOrderDto);
        else
            throw new GenericException(EcomPromptMessage.ORDER_UNSUCCESSFULL);
        return EcomApplicationResponse.success(EcomPromptMessage.ORDER_PLACED_SUCCESSFULLY);
    }

    @PostMapping("/addOrders")
    public EcomApplicationResponse saveOrders(@RequestBody List<EcomOrderDto> ecomOrdersDto) {
        for(EcomOrderDto ecomOrderDto:ecomOrdersDto){
        if (ecomService.validateOrderDto(ecomOrderDto))
            ecomService.placeOrders(ecomOrderDto);
        }
        return EcomApplicationResponse.success(EcomPromptMessage.ORDER_PLACED_SUCCESSFULLY);
    }


    //SEARCH ORDERS OF A USER WITH EMAIL OR USER-ID
    @GetMapping("/findOrders/{search}")
    public EcomApplicationResponse getOrders(@PathVariable String search) {
        return EcomApplicationResponse.success(ecomService.searchOrders(search));
    }

    //search all orders of all users
    @GetMapping("/findAllorders")
    public EcomApplicationResponse getAllOrders() {
        return EcomApplicationResponse.success(orderRepository.findAll());
    }

}
