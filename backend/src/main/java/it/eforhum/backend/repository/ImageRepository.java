package it.eforhum.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.eforhum.backend.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    List<Image> findByEventId(Integer eventId);
}
