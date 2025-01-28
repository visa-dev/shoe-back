package com.uov.exam.repo;
import com.uov.exam.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserRepo extends MongoRepository<User, String> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    User getUserByUsername(String username);
}
