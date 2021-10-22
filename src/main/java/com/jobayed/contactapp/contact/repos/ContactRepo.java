package com.jobayed.contactapp.contact.repos;

import com.jobayed.contactapp.contact.entities.Contact;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Repository
public interface ContactRepo extends JpaRepository<Contact, Long>
{
    public ArrayList<Contact> findAll();
}
