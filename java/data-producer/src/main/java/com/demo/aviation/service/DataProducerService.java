package com.demo.aviation.service;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.*;

import java.util.*;
import java.util.regex.Pattern;

import org.joda.time.DateTime;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

@Service
public class DataProducerService {


    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;



    public void executeResponse(String simulator) throws  Exception {

        DateTime startLocalDate;
        startLocalDate = new DateTime();
        long startDateUnix;
        startDateUnix = (startLocalDate.getMillis() / 1000) - 3600;


        String keyMessage = simulator + "-" +startDateUnix;


        String topic = simulator;

        String REGEX = ";";
        Pattern pattern = Pattern.compile(REGEX);
        JsonFactory fac = new JsonFactory();
        StringWriter writer=new StringWriter();
        try (BufferedReader in = new BufferedReader(new FileReader("/takeoff_climb_1.csv"));
             JsonGenerator gen = fac.createGenerator(writer);) {
            String[] headers = pattern.split(in.readLine());
            String line;
            while ((line = in.readLine()) != null) {
                gen.writeStartArray();
                gen.writeStartObject();
                String[] values = pattern.split(line);
                for (int i = 0; i < headers.length; i++) {
                    String value = i < values.length ? values[i] : null;
                    gen.writeStringField(headers[i], value);
                }
                gen.writeEndObject();
                gen.writeEndArray();

               kafkaTemplate.send(topic, keyMessage, writer.getBuffer().toString());

           }

        }

    }


}
