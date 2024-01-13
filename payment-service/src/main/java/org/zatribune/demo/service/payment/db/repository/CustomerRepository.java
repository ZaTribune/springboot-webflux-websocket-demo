package org.zatribune.demo.service.payment.db.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import org.zatribune.demo.service.payment.db.entity.Customer;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer, ObjectId> {

}
