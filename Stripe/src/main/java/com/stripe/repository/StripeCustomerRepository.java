package com.stripe.repository;

import com.stripe.entity.StripeCustomer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StripeCustomerRepository extends MongoRepository<StripeCustomer,String> {
}
