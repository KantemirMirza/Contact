package com.kani.contact.repository;

import com.kani.contact.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IContactRepository extends JpaRepository<Contact,String> {
    Optional<Contact> findContactById(String contactId);
}
