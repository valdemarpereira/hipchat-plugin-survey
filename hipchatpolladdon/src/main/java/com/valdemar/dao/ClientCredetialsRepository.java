package com.valdemar.dao;

import com.valdemar.model.ClientCredentialsData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientCredetialsRepository extends MongoRepository<ClientCredentialsData, String> {
}
