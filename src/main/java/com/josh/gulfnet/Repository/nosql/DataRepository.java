package com.josh.gulfnet.Repository.nosql;

import com.josh.gulfnet.model.Data;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends MongoRepository<Data, Integer> {
}
