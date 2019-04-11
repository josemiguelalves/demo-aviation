package com.demo.aviation.service;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.*;

import java.util.*;
import java.util.regex.Pattern;

import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

@Service
public class DataProducerService {


    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;
    String csvInput = "/takeoff_climb_1.csv";



    public void executeResponse2(String simulator) throws Exception {

        DateTime startLocalDate;
        startLocalDate = new DateTime();
        long startDateUnix;
        startDateUnix = (startLocalDate.getMillis() / 1000) - 3600;




        String REGEX = ";";
        Pattern pattern = Pattern.compile(REGEX);
        JsonFactory fac = new JsonFactory();
        StringWriter writer = new StringWriter();
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

//               String teste

                // kafkaTemplate.send(topic, keyMessage, writer.getBuffer().toString());
                System.out.print(writer.getBuffer().toString());


            }

        }

    }

    public void executeResponse3(String fligtSimulator) throws Exception {


        try (InputStream in = new FileInputStream("/takeoff_climb_1.csv");) {
            CSV csv = new CSV(true, ';', in);
            List<String> fieldNames = null;
            if (csv.hasNext()) fieldNames = new ArrayList<>(csv.next());
            List<Map<String, String>> list = new ArrayList<>();
            while (csv.hasNext()) {
                List<String> x = csv.next();
                Map<String, String> obj = new LinkedHashMap<>();
                for (int i = 0; i < fieldNames.size(); i++) {
                    obj.put(fieldNames.get(i), x.get(i));
                }
                list.add(obj);
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(System.out, list);
        }


    }


    public void executeResponse(String flightSimulator) throws Exception {


        File input = new File(csvInput);
        File output = new File("/home/jose/Desktop/output.json");

        DateTime startLocalDate;
        startLocalDate = new DateTime();
        long startDateUnix;
        startDateUnix = (startLocalDate.getMillis() / 1000) - 3600;

        String keyMessage = flightSimulator + "-" + startDateUnix;


        String topic = flightSimulator;
        int messageCount =0;

        CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).setColumnSeparator(';').build();
        CsvMapper csvMapper = new CsvMapper();

        // Read data from CSV file
//            List<Object> readAll = csvMapper.readerFor(Map.class).with(csvSchema).readValues(input).readAll();


        try (Reader reader = new FileReader(input)) {
            MappingIterator<Object> readAll = csvMapper.readerFor(Map.class).with(csvSchema).readValues(input);
            ObjectMapper mapper = new ObjectMapper();
            while (readAll.hasNext()) {
                messageCount++;
             System.out.println("Message sent to kafka: "+ startDateUnix + " message count "+messageCount + " " + flightSimulator);
//                readAll.next();
//
             kafkaTemplate.send(topic, keyMessage, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(readAll.next()));
//
// doSomeStuffWithObject(mi.next());
//
            }


            // Write JSON formated data to output.json file
//            mapper.writerWithDefaultPrettyPrinter().writeValue(output, readAll);

            // Write JSON formated data to stdout

        }


    }
}

