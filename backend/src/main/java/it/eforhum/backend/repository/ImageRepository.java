package it.eforhum.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.eforhum.backend.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    List<Image> findByEventId(Integer eventId);

    @Query("SELECT i FROM Image i LEFT JOIN FETCH i.uploader LEFT JOIN FETCH i.event WHERE i.id = :id")
    Optional<Image> findByIdWithUploader(Integer id);
}
