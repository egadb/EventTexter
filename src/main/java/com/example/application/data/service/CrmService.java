package com.example.application.data.service;

import com.example.application.data.entity.Gang;
import com.example.application.data.entity.Contact;
import com.example.application.data.repository.GangRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
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


    public void inviteAllContacts() {
        List<Contact> contacts = contactRepository.findAll();
         for(int i = 0; i < contacts.size(); i++) {
             if(contacts.get(i).getStatus().equals(""))
                {
                contacts.get(i).setStatus("Invite sent");
                Message message = Message.creator(new com.twilio.type.PhoneNumber(contacts.get(i).getPhone()),
                new com.twilio.type.PhoneNumber("+14078097360"),
                contacts.get(i).getFirstName() + ", you've been invited to an event! Reply YES/NO to confirm your attendance.")
                .create();
                }
             }
            
        contactRepository.saveAll(contacts);
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