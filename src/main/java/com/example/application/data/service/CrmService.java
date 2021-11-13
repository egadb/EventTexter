package com.example.application.data.service;

import com.example.application.data.entity.Gang;
import com.example.application.data.entity.Contact;
import com.example.application.data.repository.GangRepository;
import com.example.application.data.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service 
public class CrmService {

    private final ContactRepository contactRepository;
    private final GangRepository gangRepository;

    public CrmService(ContactRepository contactRepository,
                      GangRepository gangRepository) { 
        this.contactRepository = contactRepository;
        this.gangRepository = gangRepository;
    }

    public List<Contact> findAllContacts(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) { 
            return contactRepository.findAll();
        } else {
            return contactRepository.search(stringFilter);
        }
    }

    public long countContacts() {
        return contactRepository.count();
    }

    public void deleteContact(Contact contact) {
        contactRepository.delete(contact);
    }

    public void saveContact(Contact contact) {
        if (contact == null) { 
            System.err.println("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        contactRepository.save(contact);
    }

    public List<Gang> findAllGangs() {
        return gangRepository.findAll();
    }
}