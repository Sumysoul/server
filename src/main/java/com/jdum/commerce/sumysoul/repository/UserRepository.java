package com.jdum.commerce.sumysoul.repository;

import com.jdum.commerce.sumysoul.domain.User;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findOneByLogin(String login);
  boolean existsByLogin(String login);
}
