package com.kani.contact.service.impl;

import com.kani.contact.entity.Contact;
import com.kani.contact.repository.IContactRepository;
import com.kani.contact.service.IContactService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.kani.contact.entity.Constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ContactService implements IContactService {
    private final IContactRepository contactRepository;

    @Override
    public Page<Contact> getAllContacts(int page, int size) {
        return contactRepository.findAll(PageRequest.of(page, size, Sort.by("name")));
    }

    @Override
    public Contact getContactById(String contactId) {
        return contactRepository.findContactById(contactId)
                .orElseThrow(()-> new RuntimeException("Contact Not Found"));
    }

    @Override
    public Contact createContact(Contact contact) {
        return contactRepository.save(contact);
    }

    @Override
    public void deleteContact(Contact contact) {

    }

    @Override
    public String uploadPhoto(String id, MultipartFile file) {
        log.info("Saving Picture For Contact ID: {}", id);
        Contact contact = getContactById(id);
        String photoUrl = photoFunction.apply(id, file);
        contact.setPhotoURL(photoUrl);
        contactRepository.save(contact);
        return photoUrl;
    }

    private final Function<String,String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".png");

    private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) ->{
        try{
            Path fileLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if(!Files.exists(fileLocation)){
                Files.createDirectory(fileLocation);
            }
            Files.copy(image.getInputStream(), fileLocation.resolve(id + fileExtension.apply(image.getOriginalFilename())), REPLACE_EXISTING);
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/contacts/image/" + id + fileExtension.apply(image.getOriginalFilename())).toUriString();
        }catch (Exception ex){
            throw new RuntimeException("Unable to save image");
        }
    };
}
