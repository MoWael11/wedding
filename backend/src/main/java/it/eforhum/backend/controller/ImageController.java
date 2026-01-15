package it.eforhum.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.eforhum.backend.dto.ImageResponse;
import it.eforhum.backend.entity.User;
import it.eforhum.backend.service.ImageService;

@RestController
@RequestMapping("/api/weddings/{weddingId}/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    public ResponseEntity<ImageResponse> uploadImage(
            @PathVariable Integer weddingId,
            @RequestParam("file") MultipartFile file,
            @RequestAttribute("currentUser") User currentUser) {
        ImageResponse response = imageService.uploadImage(weddingId, file, currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ImageResponse>> getImages(@PathVariable Integer weddingId) {
        List<ImageResponse> images = imageService.getImagesByWeddingId(weddingId);
        return ResponseEntity.ok(images);
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<ImageResponse> getImage(
            @PathVariable Integer weddingId,
            @PathVariable Integer imageId) {
        ImageResponse response = imageService.getImageById(weddingId, imageId);
        return ResponseEntity.ok(response);
    }
}
