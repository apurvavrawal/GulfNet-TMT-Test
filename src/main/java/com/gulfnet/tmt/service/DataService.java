package com.gulfnet.tmt.service;

import com.gulfnet.tmt.dao.DataDao;
import com.gulfnet.tmt.model.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DataService {
    private final DataDao dataDao;

    public DataService(DataDao dataDao) {
        this.dataDao = dataDao;
    }

    public List<Data> getData() {
        return dataDao.getData();
    }

    public Data createData(Data data) {
        return dataDao.createData(data);
    }
}
