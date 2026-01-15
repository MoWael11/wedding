package it.eforhum.backend.dto;

import java.time.LocalDate;
import java.util.List;

public class WeddingResponse {

    private Integer id;
    private String code;
    private String title;
    private LocalDate eventDate;
    private List<PersonDTO> persons;

    public WeddingResponse() {}

    public WeddingResponse(Integer id, String code, String title, LocalDate eventDate, List<PersonDTO> persons) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.eventDate = eventDate;
        this.persons = persons;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public List<PersonDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonDTO> persons) {
        this.persons = persons;
    }
}
