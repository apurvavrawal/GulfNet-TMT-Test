package com.gulfnet.tmt.repository.nosql;

import com.gulfnet.tmt.entity.nosql.Data;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends MongoRepository<Data, Integer> {
}
