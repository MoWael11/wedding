package it.eforhum.backend.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.eforhum.backend.dto.AuthResponse;
import it.eforhum.backend.dto.SignInRequest;
import it.eforhum.backend.dto.SignUpRequest;
import it.eforhum.backend.entity.Token;
import it.eforhum.backend.entity.User;
import it.eforhum.backend.exception.DuplicateException;
import it.eforhum.backend.exception.UnauthenticatedException;
import it.eforhum.backend.repository.TokenRepository;
import it.eforhum.backend.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public AuthService(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public AuthResponse signUp(SignUpRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(hashPassword(request.getPassword()));
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());

        user = userRepository.save(user);

        String tokenValue = generateToken();
        Token token = new Token();
        token.setToken(tokenValue);
        token.setUser(user);
        token.setCreatedAt(LocalDateTime.now());

        tokenRepository.save(token);

        return new AuthResponse(user.getId(), tokenValue, user.getUsername(), user.getRole());
    }

    @Transactional
    public AuthResponse signIn(SignInRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UnauthenticatedException("Invalid username or password"));

        if (!verifyPassword(request.getPassword(), user.getPassword())) {
            throw new UnauthenticatedException("Invalid username or password");
        }

        String tokenValue = generateToken();
        Token token = new Token();
        token.setToken(tokenValue);
        token.setUser(user);
        token.setCreatedAt(LocalDateTime.now());

        tokenRepository.save(token);

        return new AuthResponse(user.getId(), tokenValue, user.getUsername(), user.getRole());
    }

    public Optional<User> validateToken(String tokenValue) {
        return tokenRepository.findByToken(tokenValue)
                .map(Token::getUser);
    }

    @Transactional
    public void invalidateToken(String tokenValue) {
        tokenRepository.findByToken(tokenValue)
                .ifPresent(tokenRepository::delete);
    }

    private String generateToken() {
        byte[] bytes = new byte[48];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hashPassword(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    private boolean verifyPassword(String rawPassword, String hashedPassword) {
        String hashed = Base64.getEncoder().encodeToString(rawPassword.getBytes());
        return hashed.equals(hashedPassword);
    }
}
