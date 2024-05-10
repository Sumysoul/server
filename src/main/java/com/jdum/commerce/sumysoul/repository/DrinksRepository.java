package com.jdum.commerce.sumysoul.repository;

import com.jdum.commerce.sumysoul.domain.Drinks;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrinksRepository extends MongoRepository<Drinks, String> {
}
