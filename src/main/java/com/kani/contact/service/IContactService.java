package com.kani.contact.service;

import com.kani.contact.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface IContactService {
    Page<Contact> getAllContacts(int page, int size);

     Contact getContactById(String contactId);

     Contact createContact(Contact contact);

     void deleteContact(Contact contact);

     String uploadPhoto(String id, MultipartFile file);
}
