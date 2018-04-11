package com.valdemar.dao;

import com.valdemar.model.ClientCredentialsData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientCredentialsRepository extends MongoRepository<ClientCredentialsData, String> {
}
