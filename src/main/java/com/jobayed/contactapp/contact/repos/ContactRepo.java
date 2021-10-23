package com.jobayed.contactapp.contact.repos;

import com.jobayed.contactapp.contact.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ContactRepo extends JpaRepository<Contact, Long>
{
    public ArrayList<Contact> findAll();
}
