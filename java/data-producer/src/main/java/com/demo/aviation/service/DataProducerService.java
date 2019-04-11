package com.demo.aviation.service;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.*;

import java.util.*;
import java.util.regex.Pattern;

import org.joda.time.DateTime;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DataProducerService {


    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;



    public void executeResponse(String simulator) throws  Exception {

        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm:ss");
        DateTime localDate;
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
        try (BufferedReader in = new BufferedReader(new FileReader("/home/jose/projects/demo-aviation/data-sample/takeoff_climb_1.csv"));
             JsonGenerator gen = fac.createGenerator(writer);) {
            String[] headers = pattern.split(in.readLine());
     //       gen.writeStartArray();
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

           //     String result=writer.getBuffer().toString();
                kafkaTemplate.send(topic, keyMessage, writer.getBuffer().toString());

//                System.out.println(result);
//
           }

        }

    }


}
