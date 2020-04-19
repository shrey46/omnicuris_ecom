package com.ecom.repository;

import com.ecom.model.EcomOrderModel;
import com.ecom.model.EcomUserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author shrey
 */

@Repository
public interface EcomOrderRepository extends MongoRepository<EcomOrderModel, String> {
}
