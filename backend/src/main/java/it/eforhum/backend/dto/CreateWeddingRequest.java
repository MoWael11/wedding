package it.eforhum.backend.dto;

import java.time.LocalDate;

public class CreateWeddingRequest {

    private String title;
    private LocalDate eventDate;
    private PersonDTO person1;
    private PersonDTO person2;

    public CreateWeddingRequest() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public PersonDTO getPerson1() {
        return person1;
    }

    public void setPerson1(PersonDTO person1) {
        this.person1 = person1;
    }

    public PersonDTO getPerson2() {
        return person2;
    }

    public void setPerson2(PersonDTO person2) {
        this.person2 = person2;
    }
}
