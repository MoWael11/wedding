package it.eforhum.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.eforhum.backend.entity.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("SELECT t FROM Token t JOIN FETCH t.user WHERE t.token = :token")
    Optional<Token> findByToken(String token);

    void deleteByUserId(Integer userId);
}
