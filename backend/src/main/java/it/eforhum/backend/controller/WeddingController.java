package it.eforhum.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.eforhum.backend.dto.CreateWeddingRequest;
import it.eforhum.backend.dto.WeddingResponse;
import it.eforhum.backend.entity.User;
import it.eforhum.backend.service.WeddingService;

@RestController
@RequestMapping("/api/weddings")
public class WeddingController {

    private final WeddingService weddingService;

    public WeddingController(WeddingService weddingService) {
        this.weddingService = weddingService;
    }

    @PostMapping
    public ResponseEntity<WeddingResponse> createWedding(
            @RequestBody CreateWeddingRequest request,
            @RequestAttribute("currentUser") User currentUser) {
        WeddingResponse response = weddingService.createWedding(request, currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<WeddingResponse>> getAllWeddings() {
        List<WeddingResponse> weddings = weddingService.getAllWeddings();
        return ResponseEntity.ok(weddings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeddingResponse> getWeddingById(@PathVariable Integer id) {
        WeddingResponse response = weddingService.getWeddingById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<WeddingResponse> getWeddingByCode(@PathVariable String code) {
        WeddingResponse response = weddingService.getWeddingByCode(code);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWedding(
            @PathVariable Integer id,
            @RequestAttribute("currentUser") User currentUser) {
        weddingService.deleteWedding(id, currentUser);
        return ResponseEntity.ok().build();
    }
}
