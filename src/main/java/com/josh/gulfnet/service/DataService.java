package com.josh.gulfnet.service;

import com.josh.gulfnet.dao.DataDao;
import com.josh.gulfnet.model.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataService {

    @Autowired
    private DataDao dataDao;
    public List<Data> getData() {
        return dataDao.getData();
    }

    public Data createData(Data data) {
        return dataDao.createData(data);
    }
}
