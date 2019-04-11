package com.demo.aviation.controller;

import com.demo.aviation.service.DataProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class DataProducerController {

    @Autowired
    private DataProducerService kafkaProducerService;


    @RequestMapping(value= "/start-producer",  method = GET)

    public String sendMessage (@RequestParam("flightSimulator") String flightSimulator) {

        try {

            kafkaProducerService.executeResponse(flightSimulator);

        } catch (Exception e) {

            System.out.println("Exception no Start Producer");
            System.out.println(e);
        }

        return "Start Producer";
    }

}
