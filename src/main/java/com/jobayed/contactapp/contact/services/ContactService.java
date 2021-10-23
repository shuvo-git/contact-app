package com.jobayed.contactapp.contact.services;

import com.jobayed.contactapp.contact.domain.Contact;

import java.util.List;

public interface ContactService
{
    public List<Contact> getAll();
    public Contact create(Contact contact);
}
