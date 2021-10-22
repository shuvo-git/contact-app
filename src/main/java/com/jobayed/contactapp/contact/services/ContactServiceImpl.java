package com.jobayed.contactapp.contact.services;

import com.jobayed.contactapp.contact.entities.Contact;
import com.jobayed.contactapp.contact.repos.ContactRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@RequiredArgsConstructor
@Service
public class ContactServiceImpl implements ContactService
{
    private final ContactRepo repo;

    @Override
    public List<Contact> getAll() {
        return repo.findAll();
    }

    @Override
    public Contact create(Contact contact) {
        return repo.save(contact);
    }
}
