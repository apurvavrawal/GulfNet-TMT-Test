package com.josh.gulfnet.dao;

import com.josh.gulfnet.Repository.nosql.DataRepository;
import com.josh.gulfnet.model.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataDao {
    @Autowired
    private DataRepository dataRepository;
    public List<Data> getData() {
        return dataRepository.findAll();
    }

    public Data createData(Data data) {
        return dataRepository.save(data);
    }
}
