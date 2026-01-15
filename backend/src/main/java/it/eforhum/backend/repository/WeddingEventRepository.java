package it.eforhum.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.eforhum.backend.entity.WeddingEvent;

@Repository
public interface WeddingEventRepository extends JpaRepository<WeddingEvent, Integer> {

    Optional<WeddingEvent> findByCode(String code);
}
