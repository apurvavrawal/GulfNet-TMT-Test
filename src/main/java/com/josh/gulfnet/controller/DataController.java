package com.josh.gulfnet.controller;

import com.josh.gulfnet.model.Data;
import com.josh.gulfnet.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {
    @Autowired
    private DataService dataService;

    @GetMapping("/get")
    public List<Data> getData(){
        return dataService.getData();
    }

    @PostMapping("/create")
    public Data createData(@RequestBody Data data){
        return dataService.createData(data);
    }
}
