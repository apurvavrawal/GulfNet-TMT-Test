package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.model.Data;
import com.gulfnet.tmt.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/data")
@Slf4j
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/get")
    public List<Data> getData(){
        return dataService.getData();
    }

    @PostMapping("/create")
    public Data saveData(@RequestBody Data data){
        return dataService.createData(data);
    }
}
