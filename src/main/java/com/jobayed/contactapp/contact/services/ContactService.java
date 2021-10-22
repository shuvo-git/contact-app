package com.jobayed.contactapp.contact.services;

import com.jobayed.contactapp.contact.entities.Contact;

import java.util.List;

public interface ContactService
{
    public List<Contact> getAll();
    public Contact create(Contact contact);
}
