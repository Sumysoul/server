package com.jdum.commerce.sumysoul.repository;

import com.jdum.commerce.sumysoul.domain.Food;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends MongoRepository<Food, String> {
}
