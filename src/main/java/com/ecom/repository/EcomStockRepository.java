package com.ecom.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.ecom.model.EcomStockModel;

/**
 * @author shrey
 *
 */

@Repository
public interface EcomStockRepository extends MongoRepository<EcomStockModel,String> {
	//public EcomModel findByid(String id);
}
