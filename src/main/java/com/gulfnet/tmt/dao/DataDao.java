package com.gulfnet.tmt.dao;

import com.gulfnet.tmt.repository.nosql.DataRepository;
import com.gulfnet.tmt.entity.nosql.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataDao {
    private final DataRepository dataRepository;

    public DataDao(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public List<Data> getData() {
        return dataRepository.findAll();
    }

    public Data createData(Data data) {
        return dataRepository.save(data);
    }
}
