package com.ecom.service;

import com.ecom.constant.EcomPromptMessage;
import com.ecom.dto.EcomOrderDto;
import com.ecom.dto.EcomStockDto;
import com.ecom.dto.EcomUserDto;
import com.ecom.exception.GenericException;
import com.ecom.model.EcomOrderModel;
import com.ecom.model.EcomStockModel;
import com.ecom.model.EcomUserModel;
import com.ecom.repository.EcomOrderRepository;
import com.ecom.repository.EcomStockRepository;
import com.ecom.repository.EcomUserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.diagnostics.Logger;
import org.bson.diagnostics.Loggers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shrey
 */

@Service
public class EcomServiceImpl implements EcomService {

    @Autowired
    private EcomService ecomService;

    @Autowired
    private MongoTemplate mongoOperations;

    @Autowired
    private EcomStockRepository stockRepository;

    @Autowired
    private EcomOrderRepository orderRepository;

    @Autowired
    private EcomUserRepository userRepository;

    private final Logger LOGGER = Loggers.getLogger(this.getClass().getName());

    @Override
    public List<EcomStockModel> searchProducts(String search) {
        Criteria c1 = null, c2 = null, c3 = null, c4 = null, c5 = null;
        Query query = null;
        List<EcomStockModel> ecomList = null;

        if (!org.apache.commons.lang.StringUtils.isBlank(search)) {
            c1 = Criteria.where("_id").is(search);
            c2 = Criteria.where("productName").regex(search, "i");
            c3 = Criteria.where("productBrand").regex(search, "i");
            c4 = Criteria.where("productCategory").regex(search, "i");
            c5 = Criteria.where("productSubCategory").regex(search, "i");

            query = new Query(new Criteria().orOperator(c1, c2, c3, c4, c5))
                    .with(Sort.by(Sort.Direction.ASC, "updatedAt"));
            ecomList = mongoOperations.find(query, EcomStockModel.class);
        } else
            throw new GenericException(EcomPromptMessage.INCORRECT_PARAMS);

        if (ecomList.size() == 0 || ecomList == null)
            throw new GenericException(EcomPromptMessage.INCORRECT_PARAMS);
        return ecomList;
    }

    @Override
    public List<EcomOrderModel> searchOrders(String search) {
        Criteria c1 = null, c2 = null;
        Query query = null;
        List<EcomOrderModel> ecomList = null;
        if (!org.apache.commons.lang.StringUtils.isBlank(search)) {
            c1 = Criteria.where("userDetails.userId").is(search);
            c2 = Criteria.where("userEmail").is(search);
            query = new Query(new Criteria().orOperator(c1, c2))
                    .with(Sort.by(Sort.Direction.ASC, "orderedAt"));
            ecomList = mongoOperations.find(query, EcomOrderModel.class);
        } else
            throw new GenericException(EcomPromptMessage.INCORRECT_PARAMS);

        if (ecomList.size() == 0 || ecomList == null)
            throw new GenericException(EcomPromptMessage.SEARCH_EMPTY);
        return ecomList;
    }

    @Override
    public boolean validateOrderDto(EcomOrderDto ecomOrderDto) {

        EcomStockModel stock = stockRepository.findById(ecomOrderDto.getProductId())
                .orElseThrow(() -> new GenericException(EcomPromptMessage.INVALID_PRODUCT_ID));
        Query query = new Query();
        query.addCriteria(Criteria.where("userEmail").is(ecomOrderDto.getUserEmail()));
        List<EcomUserModel> user = mongoOperations.find(query, EcomUserModel.class);
        if (user.size() == 0 || user == null)
            throw new GenericException(EcomPromptMessage.INVALID_USER_ID);

        if (org.apache.commons.lang.StringUtils.isBlank(ecomOrderDto.getPaymentMode())
                || ecomOrderDto.getPaymentMode() == null)
            throw new GenericException(EcomPromptMessage.PAYMENT_MODE_MISSING);

        if (ecomOrderDto.getProductQuantity() <= 0)
            throw new GenericException(EcomPromptMessage.CART_EMPTY);

        if (stock.getProductStock() >= ecomOrderDto.getProductQuantity()
                && stock.getProductStock() > 0) {
            stock.setId(ecomOrderDto.getProductId());
            stock.setProductStock(stock.getProductStock() - ecomOrderDto.getProductQuantity());
            long time = System.currentTimeMillis();
            stock.setUpdatedAt(time);
            Date date = new java.util.Date(time);
            stockRepository.save(stock);
        } else
            throw new GenericException(EcomPromptMessage.ORDER_OUT_OF_STOCK);
        return true;
    }

    @Override
    public void placeOrders(EcomOrderDto ecomOrderDto) {
        EcomStockModel stock = stockRepository.findById(ecomOrderDto.getProductId())
                .orElseThrow(() -> new GenericException(EcomPromptMessage.INVALID_PRODUCT_ID));

        Query query = new Query();
        query.addCriteria(Criteria.where("userEmail").is(ecomOrderDto.getUserEmail()));
        List<EcomUserModel> user = mongoOperations.find(query, EcomUserModel.class);
        if (user.size() == 0 || user == null)
            throw new GenericException(EcomPromptMessage.INVALID_USER_ID);
        long time = System.currentTimeMillis();
        EcomOrderModel.UserDetails userDetails = EcomOrderModel.UserDetails.builder()
                .userAddress(user.get(0).getUserAddress()).userContact(user.get(0).getUserContact())
                .userId(user.get(0).getId()).build();

        EcomOrderModel.ProductDetails productDetails = EcomOrderModel.ProductDetails.builder()
                .productName(stock.getProductName())
                .productBrand(stock.getProductBrand())
                .productCategory(stock.getProductCategory())
                .productSubCategory(stock.getProductSubCategory())
                .productQuantity(ecomOrderDto.getProductQuantity())
                .productPrice(stock.getProductPrice())
                .build();

        LOGGER.info(EcomPromptMessage.ORDER_PLACED_SUCCESSFULLY);
        EcomOrderModel ecomOrderModel = EcomOrderModel.builder()
                .userDetails(userDetails).userEmail(ecomOrderDto.getUserEmail())
                .productDetails(productDetails).productId(ecomOrderDto.getProductId())
                .orderedAt(time).deliverBy(time+7*24*60*60*1000)
                .orderTotal(ecomOrderDto.getProductQuantity() * stock.getProductPrice())
                .paymentMode(ecomOrderDto.getPaymentMode().toUpperCase()).build();
        orderRepository.save(ecomOrderModel);

    }

    @Override
    public void userValidations(EcomUserDto ecomUserDto) {

        if (org.apache.commons.lang.StringUtils.isBlank(ecomUserDto.getUserEmail())
                || org.apache.commons.lang.StringUtils.isBlank(ecomUserDto.getUserName())
                || ecomUserDto.getUserName() == null
                || ecomUserDto.getUserEmail() == null)
            throw new GenericException(EcomPromptMessage.INCORRECT_PARAMS);

        Query query = new Query();
        query.addCriteria(Criteria.where("userEmail").is(ecomUserDto.getUserEmail()));
        List<EcomUserModel> user = mongoOperations.find(query, EcomUserModel.class);
        if (user.size() != 0)
            throw new GenericException(EcomPromptMessage.USER_EXISTS);

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(ecomUserDto.getUserEmail()).matches())
            throw new GenericException(EcomPromptMessage.INCORRECT_MAIL);
        LOGGER.info("Email validated");
        EcomUserModel ecomUserModel = EcomUserModel.builder().userName(ecomUserDto.getUserName())
                .userEmail(ecomUserDto.getUserEmail())
                .userAddress(ecomUserDto.getUserAddress()).userContact(ecomUserDto.getUserContact())
                .userCreatedAt(System.currentTimeMillis()).build();
        userRepository.save(ecomUserModel);
    }

    @Override
    public void updateProducts(String id,EcomStockDto ecomStockDto) {
        EcomStockModel stock = stockRepository.findById(id)
                .orElseThrow(() -> new GenericException(EcomPromptMessage.INVALID_PRODUCT_ID));
        
        stock.setId(id);
        stock.setUpdatedAt(System.currentTimeMillis());

        if(ecomStockDto.getProductBrand()!=null)
           stock.setProductBrand(ecomStockDto.getProductBrand());

        if(ecomStockDto.getProductName()!=null)
            stock.setProductName(ecomStockDto.getProductName());

        if (ecomStockDto.getProductStock()!=0)
            stock.setProductStock(ecomStockDto.getProductStock());

        if(ecomStockDto.getProductPrice()>0)
            stock.setProductPrice(ecomStockDto.getProductPrice());

        if(ecomStockDto.getProductSellerId()!=null)
            stock.setProductSellerId(ecomStockDto.getProductSellerId());

        if(ecomStockDto.getProductRatings()!=null)
            stock.setProductRatings(ecomStockDto.getProductRatings());

        if(ecomStockDto.getProductCategory()!=null)
            stock.setProductCategory(ecomStockDto.getProductCategory());

        if(ecomStockDto.getProductSubCategory()!=null)
            stock.setProductSubCategory(ecomStockDto.getProductSubCategory());

        stockRepository.save(stock);

    }


}
