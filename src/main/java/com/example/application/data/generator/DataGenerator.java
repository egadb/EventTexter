package com.example.application.data.generator;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.List;
import java.util.stream.Collectors;


import com.example.application.data.entity.Gang;
import com.example.application.data.entity.Contact;
import com.example.application.data.repository.ContactRepository;
import com.example.application.data.repository.GangRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import com.vaadin.exampledata.ChanceStringType;
import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(ContactRepository contactRepository, GangRepository gangRepository) {

        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (contactRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");
            ExampleDataGenerator<Gang> gangGenerator = new ExampleDataGenerator<>(Gang.class,
                    LocalDateTime.now());
            gangGenerator.setData(Gang::setName, new ChanceStringType("company"));
            List<Gang> gangs = gangRepository.saveAll(gangGenerator.create(5, seed));

            logger.info("... generating 50 Contact entities...");
            ExampleDataGenerator<Contact> contactGenerator = new ExampleDataGenerator<>(Contact.class,
                    LocalDateTime.now());
            contactGenerator.setData(Contact::setFirstName, DataType.FIRST_NAME);
            contactGenerator.setData(Contact::setLastName, DataType.LAST_NAME);
            //contactGenerator.setData(Contact::setPhone, DataType.PHONE_NUMBER);


            Random r = new Random(seed);
            List<Contact> contacts = contactGenerator.create(50, seed).stream().map(contact -> {
                contact.setGang(gangs.get(r.nextInt(gangs.size())));
                contact.setStatus("");
                contact.setPhone("8196791259");
                return contact;
            }).collect(Collectors.toList());

            contactRepository.saveAll(contacts);

            logger.info("Generated demo data");
        };
    }

}
