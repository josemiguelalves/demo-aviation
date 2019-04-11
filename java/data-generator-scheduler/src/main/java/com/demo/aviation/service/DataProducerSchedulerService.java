package com.demo.aviation.service;

import org.joda.time.DateTime;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;


@Service
@EnableConfigurationProperties
public class DataProducerSchedulerService {

    public void startProcessingDataDemoAviation() {
        DateTime localDate;

        long localDateUnix;

        RestTemplate restTemplate;


        Map<String, String> params;


        while (true) {


            localDate = new DateTime();
            localDateUnix = localDate.getMillis() / 1000;


            for (int i = 0; i < 10; i++) {

                System.out.println("Request sent localdateUnix: " + localDateUnix);

                params = new HashMap<String, String>();
                params.put("flightSimulator", "flightSimulator-"+Integer.toString(i));


                restTemplate = new RestTemplate();

                try {

                    restTemplate.getForObject("http://data-producer:8115/start-producer?flightSimulator={flightSimulator}", String.class, params);

                } catch (Exception e) {
                    System.out.println(e);
                }


                try {
                    sleep(1000);
                } catch (Exception e) {
                    System.out.println(e);
                }

        }
   }
}


}
