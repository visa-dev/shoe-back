package com.uov.exam.repo;

import com.uov.exam.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface OrderRepo extends MongoRepository<Order, String> {

    @Query("{'username': ?0}")
    List<Order> findByUsername(String username);


}
