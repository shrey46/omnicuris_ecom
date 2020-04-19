package com.ecom.repository;
import com.ecom.model.EcomUserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.ecom.model.EcomStockModel;

import java.util.Optional;

/**
 * @author shrey
 *
 */

@Repository
public interface EcomUserRepository extends MongoRepository<EcomUserModel,String>{
    @Query("{'userEmail : ?0'}")
    EcomUserModel findUserByMail(String userEmail);
}
