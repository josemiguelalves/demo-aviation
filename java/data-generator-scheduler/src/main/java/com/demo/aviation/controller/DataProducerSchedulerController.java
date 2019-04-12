package com.demo.aviation.controller;

import com.demo.aviation.service.DataProducerSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@EnableAsync
public class DataProducerSchedulerController {

    @Autowired
    DataProducerSchedulerService dataProducerSchedulerService;

@Async
    @RequestMapping(value= "/start-requester-demo-aviation",  method = GET)
    public void  startProcessingDataDemoAviation() {

        System.out.println("startProcessingDemoAviation");

        dataProducerSchedulerService.startProcessingDataDemoAviation();

    }

}
