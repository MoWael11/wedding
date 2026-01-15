package it.eforhum.backend.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.eforhum.backend.dto.CreateWeddingRequest;
import it.eforhum.backend.dto.PersonDTO;
import it.eforhum.backend.dto.WeddingResponse;
import it.eforhum.backend.entity.Person;
import it.eforhum.backend.entity.User;
import it.eforhum.backend.entity.WeddingEvent;
import it.eforhum.backend.exception.NotFoundException;
import it.eforhum.backend.exception.UnauthorizedException;
import it.eforhum.backend.repository.WeddingEventRepository;

@Service
public class WeddingService {

    private final WeddingEventRepository weddingEventRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public WeddingService(WeddingEventRepository weddingEventRepository) {
        this.weddingEventRepository = weddingEventRepository;
    }

    @Transactional
    public WeddingResponse createWedding(CreateWeddingRequest request, User owner) {
        WeddingEvent wedding = new WeddingEvent();
        wedding.setCode(generateCode());
        wedding.setTitle(request.getTitle());
        wedding.setEventDate(request.getEventDate());
        wedding.setTotalBytes(0);
        wedding.setCreatedAt(LocalDateTime.now());
        wedding.setOwner(owner);

        Person person1 = new Person();
        person1.setFirstName(request.getPerson1().getFirstName());
        person1.setLastName(request.getPerson1().getLastName());
        person1.setCreatedAt(LocalDateTime.now());
        person1.setWeddingEvent(wedding);
        wedding.getPersons().add(person1);

        Person person2 = new Person();
        person2.setFirstName(request.getPerson2().getFirstName());
        person2.setLastName(request.getPerson2().getLastName());
        person2.setCreatedAt(LocalDateTime.now());
        person2.setWeddingEvent(wedding);
        wedding.getPersons().add(person2);

        wedding = weddingEventRepository.save(wedding);

        return toResponse(wedding);
    }

    public WeddingResponse getWeddingByCode(String code) {
        WeddingEvent wedding = weddingEventRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Wedding not found"));
        return toResponse(wedding);
    }

    public WeddingResponse getWeddingById(Integer id) {
        WeddingEvent wedding = weddingEventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Wedding not found"));
        return toResponse(wedding);
    }

    public List<WeddingResponse> getAllWeddings() {
        return weddingEventRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void deleteWedding(Integer id, User currentUser) {
        WeddingEvent wedding = weddingEventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Wedding not found"));

        if (!wedding.getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not the owner of this wedding");
        }

        weddingEventRepository.delete(wedding);
    }

    private WeddingResponse toResponse(WeddingEvent wedding) {
        List<PersonDTO> persons = wedding.getPersons().stream()
                .map(p -> new PersonDTO(p.getFirstName(), p.getLastName()))
                .toList();

        return new WeddingResponse(
                wedding.getId(),
                wedding.getCode(),
                wedding.getTitle(),
                wedding.getEventDate(),
                persons
        );
    }

    private String generateCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            code.append(chars.charAt(secureRandom.nextInt(chars.length())));
        }
        return code.toString();
    }
}
